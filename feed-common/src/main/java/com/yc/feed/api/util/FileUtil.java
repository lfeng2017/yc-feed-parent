package com.yc.feed.api.util;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.DictConfig;

import java.io.*;

/**
 * Created by Administrator on 2017/3/24.
 */
public class FileUtil {
    //private static String commentLocalFile = "F:\\commentdetail.txt";
    private static String commentLocalFile = ConfigManager.get(DictConfig.class).getDictMap().get("commentLocalFile");
    private static File file = new File(commentLocalFile);

    public static void writeDataToFile(String content) throws Exception{
        //FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
       // BufferedWriter bw = new BufferedWriter(fw);
        FileOutputStream writerStream = new FileOutputStream(file,true);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
        try{
            writer.write(content);
            writer.newLine();
        }finally {
            writer.close();
        }
    }
}
