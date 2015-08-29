package com.mrzhu.webboot.util;

import com.mrzhu.webboot.dto.TableQuery;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;

/**
 * Created by mrzhu on 8/29/15.
 */
public class ActionUtil {
    public static TableQuery parseTableQuery(Map<String, String> param) {
        TableQuery query = new TableQuery();
        if (param != null) {
            query.setDraw(param.get("draw"));

            int start = NumberUtils.toInt(param.get("start"), 0);
            query.setStart(start);

            int length = NumberUtils.toInt(param.get("length"), -1);
            if(length>0) {
                query.setLength(length);
            }

            int orderColumn = NumberUtils.toInt(param.get("order[0][column]"), 0);
            query.setOrderColumn(orderColumn);

            query.setOrderDir(param.get("order[0][dir]"));
        }

        return query;
    }
}
