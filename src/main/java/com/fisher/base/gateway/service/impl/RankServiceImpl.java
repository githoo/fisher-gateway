package com.fisher.base.gateway.service.impl;


import com.fisher.base.gateway.util.XGBUtils;
import com.fisher.base.gateway.service.ProvideService;

import java.util.Map;

/**
 * Created by fisher
 */
public class RankServiceImpl implements ProvideService {

    @Override
    public String excute(Map<String, String> param, String postData) {

        // use DataLoader.CSRSparseData
        String line0 = "0 1:1 9:1 19:1 21:1 24:1 34:1 36:1 39:1 42:1 53:1 56:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 117:1 122:1";
        String line1 = "1 3:1 9:1 19:1 21:1 30:1 34:1 36:1 40:1 41:1 53:1 58:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 118:1 124:1";
        String line2 = "0 1:1 9:1 20:1 21:1 24:1 34:1 36:1 39:1 41:1 53:1 56:1 65:1 69:1 77:1 86:1 88:1 92:1 95:1 102:1 106:1 117:1 122:1";

        String [] lines = {line0,line1,line2};

        float[][] preds = XGBUtils.xgbMultiPredict(lines);
        String rs = "{";

        for (int i = 0; i < preds.length; i++) {
            float[] pred = preds[i];
            for (int j = 0; j < pred.length; j++) {
                rs += "[i: " + i + " score: " + pred[j]+"],";
            }
        }
        rs = rs.substring(0,rs.length()-1);
        rs += "}";

        return rs;
    }
}
