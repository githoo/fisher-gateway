package com.fisher.base.gateway.util;

import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;
import ml.dmlc.xgboost4j.java.XGBoostError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * xgboost 工具类
 * create by fisher
 */
public class XGBUtils {
    public static final Logger logger = LoggerFactory.getLogger(XGBUtils.class);
    public static  Booster booster;

    public static void init(String modelPath) {
        try {
            //reload model and data
            booster = XGBoost.loadModel(modelPath);
        } catch (Exception e) {
            logger.error("XGBUtils init error", e);
        }
    }

    /**
     * 单样本预测
     * @param line
     * @return
     */
    public static float[][] xgbSinglePredict(String line)  {
        try {
            XGBDataLoader.CSRSparseData spData = XGBDataLoader.loadSample(line);
            DMatrix lineDMatrix = new DMatrix(spData.rowHeaders, spData.colIndex, spData.data,
                    DMatrix.SparseType.CSR);
            float[][] preds = booster.predict(lineDMatrix);
            return preds;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XGBoostError xgBoostError) {
            xgBoostError.printStackTrace();
        }
        return null;
    }

    /**
     * 多样本预测
     * @param lines
     * @return
     */
    public static float[][] xgbMultiPredict(String[] lines)  {
        try {
            XGBDataLoader.CSRSparseData spDatas = XGBDataLoader.loadMultiSampe(lines);
            DMatrix linesDMatrix = new DMatrix(spDatas.rowHeaders, spDatas.colIndex, spDatas.data,
                    DMatrix.SparseType.CSR);
            float[][] preds = booster.predict(linesDMatrix);
            return preds;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XGBoostError xgBoostError) {
            xgBoostError.printStackTrace();
        }
        return null;
    }


    public static boolean checkPredicts(float[][] fPredicts, float[][] sPredicts) {
        if (fPredicts.length != sPredicts.length) {
            return false;
        }

        for (int i = 0; i < fPredicts.length; i++) {
            if (!Arrays.equals(fPredicts[i], sPredicts[i])) {
                return false;
            }
        }

        return true;
    }

    public static void saveDumpModel(String modelPath, String[] modelInfos) throws IOException {
        try{
            PrintWriter writer = new PrintWriter(modelPath, "UTF-8");
            for(int i = 0; i < modelInfos.length; ++ i) {
                writer.print("booster[" + i + "]:\n");
                writer.print(modelInfos[i]);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * xgb模型生成
     * @param trainPath
     * @param testPath
     * @param modelPath
     */
    public  static  void xgbModelTrain(String trainPath,String testPath,String modelPath)  {

        try{
            DMatrix trainMat = new DMatrix(trainPath);
            DMatrix testMat = new DMatrix(testPath);

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("eta", 1.0);
            params.put("max_depth", 2);
            params.put("silent", 1);
            params.put("objective", "binary:logistic");


            HashMap<String, DMatrix> watches = new HashMap<String, DMatrix>();
            watches.put("train", trainMat);
            watches.put("test", testMat);

            //set round
            int round = 2;

            //train a boost model
            Booster booster = XGBoost.train(trainMat, params, round, watches, null, null);

            //save model to modelPath
            File file = new File(modelPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String modelPathx = modelPath + "/xgb.model";
            booster.saveModel(modelPathx);
        } catch (XGBoostError xgBoostError) {
            xgBoostError.printStackTrace();
        }


    }

    /**
     * xgb模型生成
     * @param trainPath
     * @param testPath
     * @param modelPath
     */
    public  static  void xgbModelTrainByCSRS(String trainPath,String testPath,String modelPath)  {
        try{
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("eta", 1.0);
            params.put("max_depth", 2);
            params.put("silent", 1);
            params.put("objective", "binary:logistic");
            //set round
            int round = 2;
            System.out.println("start build dmatrix from csr sparse data ...");
            //build dmatrix from CSR Sparse Matrix
            XGBDataLoader.CSRSparseData spDataTrain = XGBDataLoader.loadSVMFile(trainPath);
            DMatrix trainMat = new DMatrix(spDataTrain.rowHeaders, spDataTrain.colIndex, spDataTrain.data,
                    DMatrix.SparseType.CSR);
            trainMat.setLabel(spDataTrain.labels);

            XGBDataLoader.CSRSparseData spDataTest = XGBDataLoader.loadSVMFile(testPath);
            DMatrix testMat = new DMatrix(spDataTest.rowHeaders, spDataTest.colIndex, spDataTest.data,
                    DMatrix.SparseType.CSR);
            testMat.setLabel(spDataTest.labels);

            //specify watchList
            HashMap<String, DMatrix> watches = new HashMap<String, DMatrix>();
            watches.put("train", trainMat);
            watches.put("test", testMat);
            Booster booster = XGBoost.train(trainMat, params, round, watches, null, null);

            //save model to modelPath
            File file = new File(modelPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String modelPathx = modelPath + "/xgb.model";
            booster.saveModel(modelPathx);
        } catch (IOException e) {
                e.printStackTrace();
        } catch (XGBoostError xgBoostError) {
            xgBoostError.printStackTrace();
        }


    }

    public static void main(String[] args) throws XGBoostError {

        String trainPath = "src/main/resources/model/agaricus.txt.train";
        String testPath = "src/main/resources/model/agaricus.txt.test";
        String modelPathx = "src/main/resources/model/";
        xgbModelTrain(trainPath,testPath,modelPathx);
//        xgbModelTrainByCSRS(trainPath,testPath,modelPathx);


        String modelPath = "src/main/resources/model/xgb.model";
        XGBUtils.init(modelPath);

        String line = "0 3:1 9:1 19:1 21:1 30:1 34:1 36:1 40:1 41:1 53:1 58:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 118:1 124:1";

        float[][] preds = xgbSinglePredict(line);

        for (int i = 0; i < preds.length; i++) {
            float[] pred = preds[i];
            for (int j = 0; j < pred.length; j++) {
                System.out.println("i: " + i + " pred: " + pred[j]);

            }
        }
        // use DataLoader.CSRSparseData
        String line0 = "0 1:1 9:1 19:1 21:1 24:1 34:1 36:1 39:1 42:1 53:1 56:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 117:1 122:1";
        String line1 = "1 3:1 9:1 19:1 21:1 30:1 34:1 36:1 40:1 41:1 53:1 58:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 118:1 124:1";
        String line2 = "0 1:1 9:1 20:1 21:1 24:1 34:1 36:1 39:1 41:1 53:1 56:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 117:1 122:1";

        String [] lines = {line0,line1,line2};

        float[][] preds2 = xgbMultiPredict(lines);
        for (int i = 0; i < preds2.length; i++) {
            float[] pred = preds2[i];
            for (int j = 0; j < pred.length; j++) {
                System.out.println("i: " + i + " pred: " + pred[j]);

            }
        }


    }
}
