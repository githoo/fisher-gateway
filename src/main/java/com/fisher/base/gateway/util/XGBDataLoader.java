package com.fisher.base.gateway.util;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * xgboost sample dataload
 *
 * create by fisher
 */
public class XGBDataLoader {
    public static class DenseData {
        public float[] labels;
        public float[] data;
        public int nrow;
        public int ncol;
    }

    public static class CSRSparseData {
        public float[] labels;
        public float[] data;
        public long[] rowHeaders;
        public int[] colIndex;
    }

    public static DenseData loadCSVFile(String filePath) throws IOException {
        DenseData denseData = new DenseData();

        File f = new File(filePath);
        FileInputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        denseData.nrow = 0;
        denseData.ncol = -1;
        String line;
        List<Float> tlabels = new ArrayList<Float>();
        List<Float> tdata = new ArrayList<Float>();

        while ((line = reader.readLine()) != null) {
            String[] items = line.trim().split(",");
            if (items.length == 0) {
                continue;
            }
            denseData.nrow++;
            if (denseData.ncol == -1) {
                denseData.ncol = items.length - 1;
            }

            tlabels.add(Float.valueOf(items[items.length - 1]));
            for (int i = 0; i < items.length - 1; i++) {
                tdata.add(Float.valueOf(items[i]));
            }
        }

        reader.close();
        in.close();

        denseData.labels = ArrayUtils.toPrimitive(tlabels.toArray(new Float[tlabels.size()]));
        denseData.data = ArrayUtils.toPrimitive(tdata.toArray(new Float[tdata.size()]));

        return denseData;
    }

    public static CSRSparseData loadSVMFile(String filePath) throws IOException {
        CSRSparseData spData = new CSRSparseData();

        List<Float> tlabels = new ArrayList<Float>();
        List<Float> tdata = new ArrayList<Float>();
        List<Long> theaders = new ArrayList<Long>();
        List<Integer> tindex = new ArrayList<Integer>();

        File f = new File(filePath);
        FileInputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line;
        long rowheader = 0;
        theaders.add(rowheader);
        while ((line = reader.readLine()) != null) {
            String[] items = line.trim().split(" ");
            if (items.length == 0) {
                continue;
            }

            rowheader += items.length - 1;
            theaders.add(rowheader);
            tlabels.add(Float.valueOf(items[0]));

            for (int i = 1; i < items.length; i++) {
                String[] tup = items[i].split(":");
                assert tup.length == 2;

                tdata.add(Float.valueOf(tup[1]));
                tindex.add(Integer.valueOf(tup[0]));
            }
        }

        spData.labels = ArrayUtils.toPrimitive(tlabels.toArray(new Float[tlabels.size()]));
        spData.data = ArrayUtils.toPrimitive(tdata.toArray(new Float[tdata.size()]));
        spData.colIndex = ArrayUtils.toPrimitive(tindex.toArray(new Integer[tindex.size()]));
        spData.rowHeaders = ArrayUtils.toPrimitive(theaders.toArray(new Long[theaders.size()]));

        return spData;
    }


    public static CSRSparseData loadSample(String line) throws IOException {
        CSRSparseData spData = new CSRSparseData();

        List<Float> tlabels = new ArrayList<Float>();
        List<Float> tdata = new ArrayList<Float>();
        List<Long> theaders = new ArrayList<Long>();
        List<Integer> tindex = new ArrayList<Integer>();

        long rowheader = 0;
        theaders.add(rowheader);
        String[] items = line.trim().split(" ");

        rowheader += items.length - 1;
        theaders.add(rowheader);
        tlabels.add(Float.valueOf(items[0]));

        for (int i = 1; i < items.length; i++) {
                String[] tup = items[i].split(":");
                tdata.add(Float.valueOf(tup[1]));
                tindex.add(Integer.valueOf(tup[0]));

        }

        spData.labels = ArrayUtils.toPrimitive(tlabels.toArray(new Float[tlabels.size()]));
        spData.data = ArrayUtils.toPrimitive(tdata.toArray(new Float[tdata.size()]));
        spData.colIndex = ArrayUtils.toPrimitive(tindex.toArray(new Integer[tindex.size()]));
        spData.rowHeaders = ArrayUtils.toPrimitive(theaders.toArray(new Long[theaders.size()]));

        return spData;
    }

    public static CSRSparseData loadMultiSampe(String[] lines) throws IOException {
        CSRSparseData spData = new CSRSparseData();

        List<Float> tlabels = new ArrayList<Float>();
        List<Float> tdata = new ArrayList<Float>();
        List<Long> theaders = new ArrayList<Long>();
        List<Integer> tindex = new ArrayList<Integer>();


        String line;
        long rowheader = 0;
        theaders.add(rowheader);
        for(int k = 0;k < lines.length;k++) {
            line = lines[k];
            String[] items = line.trim().split(" ");
            if (items.length == 0) {
                continue;
            }

            rowheader += items.length - 1;
            theaders.add(rowheader);
            tlabels.add(Float.valueOf(items[0]));

            for (int i = 1; i < items.length; i++) {
                String[] tup = items[i].split(":");
                assert tup.length == 2;

                tdata.add(Float.valueOf(tup[1]));
                tindex.add(Integer.valueOf(tup[0]));
            }
        }

        spData.labels = ArrayUtils.toPrimitive(tlabels.toArray(new Float[tlabels.size()]));
        spData.data = ArrayUtils.toPrimitive(tdata.toArray(new Float[tdata.size()]));
        spData.colIndex = ArrayUtils.toPrimitive(tindex.toArray(new Integer[tindex.size()]));
        spData.rowHeaders = ArrayUtils.toPrimitive(theaders.toArray(new Long[theaders.size()]));

        return spData;
    }
}