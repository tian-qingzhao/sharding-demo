package com.tqz.sharding.chapter12.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GenIdUtil {

    /**
     * 构建基因后的id
     *
     * @param genId    基因id
     * @param snowId   转换前的id
     * @param shardNum 分片数量
     * @return 新的Id
     */
    public long buildGenId(long genId, long snowId, int shardNum) {
        String gen = getGen(genId, shardNum);
        return getGenId(snowId, gen);
    }

    /**
     * 获取分片基因
     *
     * @param oldId    id
     * @param shardNum 分片数量
     * @return 分片基因
     */
    private String getGen(long oldId, int shardNum) {
        //获取分片键的对数 = log(shardNum,2)
        double logarithm = Logarithm.log(shardNum, 2);
        if (!Logarithm.isInt(logarithm)) {
            System.out.println(logarithm);
            throw new IllegalArgumentException("只支持2的n次方分片");
        }
        int genLength = (int) logarithm;
        String binaryString = Long.toBinaryString(oldId);
        return binaryString.substring(binaryString.length() - genLength);
    }

    private Long getGenId(long snowId, String gen) {
        String snowBit = Long.toBinaryString(snowId);
        String substring = snowBit.substring(0, snowBit.length() - gen.length());
        String newBinaryString = substring + gen;
        return Long.parseLong(newBinaryString, 2);
    }
}
