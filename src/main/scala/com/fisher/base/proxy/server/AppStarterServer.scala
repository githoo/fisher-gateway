
package com.fisher.base.proxy.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.{HttpRequest, _}
import akka.stream.ActorMaterializer
import com.fisher.base.gateway.core.ProxyServer
import com.fisher.base.gateway.service.ProvideService
import com.fisher.base.gateway.service.impl.{DefaultServiceImpl, RankServiceImpl}
import com.fisher.base.gateway.util.{BaseRateLimiter, Const}

import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._

/**
 * 启动类
  * Created by fisher
 */
object AppStarterServer {

  val logger : Logger  = LoggerFactory.getLogger(AppStarterServer.getClass)
  val tool =new ProxyServer()
  val limitTool = new BaseRateLimiter(tool.getLimitQPS())

  def main(args: Array[String]) {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future map/flatmap in the end
    implicit val executionContext = system.dispatcher

    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(GET, uri, head, entity, protocol) =>
        HttpResponse(entity = doGetService(uri,entity))

      case HttpRequest(POST, uri, head, entity, protocol) =>
        HttpResponse(entity = doPostService(uri,entity))

      case r: HttpRequest =>
        r.discardEntityBytes() // important to drain incoming HTTP Entity stream
        HttpResponse(404, entity = "Unknown resource!")
    }

    val bindingFuture = Http().bindAndHandleSync(requestHandler, "0.0.0.0", 8081)
    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
    //http://localhost:8080/rank

  }

  def doGetService(uri : Uri , requestEntity : RequestEntity): String ={
    var param = Map[String,String]()
    if(uri.toString().contains("?")){
      val cutedUri = uri.toString().split("\\?",2)
      if(cutedUri.length > 1){
        val uriParam = cutedUri(1)
        uriParam.split("&").foreach(pair => {
          val paraKV = pair.split("=",2)
          if(paraKV.length == 2){
            param += (paraKV(0) -> paraKV(1))
          }else{
            param += (paraKV(0) -> "")
          }
        })
      }
    }
    if(logger.isDebugEnabled()){
      logger.debug("uri:"+uri)
      logger.debug("param:"+param)
    }
//    val querydata = param.get("query").get.toString.replace("%7B","{").replace("%7D","}").replace("%22","\"")
//    logger.info("querydata::"+querydata.replace("%7B","{").replace("%7D","}").replace("%22","\""));
    dispatcher(uri.toString(),param,null)
  }

  def doPostService(uri : Uri , requestEntity : RequestEntity): String ={


    try{
      if(limitTool.tryAcquire()){
        var param = Map[String,String]()
        if(uri.toString().contains("?")){
          val cutedUri = uri.toString().split("\\?",2)
          if(cutedUri.length > 1){
            val uriParam = cutedUri(1)
            uriParam.split("&").foreach(pair => {
              val paraKV = pair.split("=",2)
              if(paraKV.length == 2){
                param += (paraKV(0) -> paraKV(1))
              }else{
                param += (paraKV(0) -> "")
              }
            })
          }
        }
        val contentData = requestEntity.asInstanceOf[HttpEntity.Strict].data.utf8String
        if(logger.isDebugEnabled()){
          logger.debug("uri"+uri)
          logger.debug("param"+param)
          logger.debug("contentData"+contentData)
        }
        dispatcher(uri.toString(),param,contentData)
      }else{
        Const.limit_result
      }
    }catch {
      case e:Exception => {
        e.toString
      }
    }finally {

    }
  }

  def getProvideService(methodName:String):ProvideService = {
    //根据方法判断请求的服务

    methodName match{
      case "rank" => new RankServiceImpl()
      case _ => new DefaultServiceImpl()
    }

  }

  def dispatcher(uri:String,param:Map[String,String],postData:String): String ={

    try{
      val methodName = uri.split("\\?")(0).split("//")(1).split("/")(1).toString()
      val provideService = getProvideService(methodName)
      val response  = provideService.excute(param,postData);
      return  response
    }catch {
      case e: Exception => {

        e.toString
      }
    }
  }
}

