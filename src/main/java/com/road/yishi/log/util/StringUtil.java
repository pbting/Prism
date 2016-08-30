package com.road.yishi.log.util;

import java.net.URL;

public class StringUtil {
    private StringUtil() {

    }

    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    public static String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }

    /**
     * "cn.fh.lightning" -> "cn/fh/lightning"
     * @param name
     * @return
     */
    public static String dotToSplash(String name) {
        return name.replaceAll("\\.", "/");
    }

    /**
     * "Apple.class" -> "Apple"
     */
    public static String trimExtension(String name) {
        int pos = name.indexOf('.');
        if (-1 != pos) {
            return name.substring(0, pos);
        }

        return name;
    }

    /**
     * /application/home -> /home
     * @param uri
     * @return
     */
    public static String trimURI(String uri) {
        String trimmed = uri.substring(1);
        int splashIndex = trimmed.indexOf('/');

        return trimmed.substring(splashIndex);
    }
    
    public static boolean isEmpty(String src){
    	return (src == null || src.trim().length()<=0) ? true : false;
    }
   
    public static boolean isNumeric(String src){
    	if (src.matches("^(-?[1-9]\\d{0,9}|0)$")) {
			return true;
		} else {
			return false;
		}
    }
    
    public static void main(String[] args) {
    	StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT * FROM (SELECT `Uuid`,`TeamUid`,`UserId`,`ServerName`,`NickName`,`UserKeys`,`Job`,`SexJob`,`Grades`,`FashionArm`,`FashionWing`,`FashionCloth`,`FashionHair`,");
		sqlBuffer.append(" `FashionHat`,`Arm`,`IsVip`,`FightPower`,`ReadyScore`,`ReadyOrder`,`FinalScore`,`FinalOrder`,`ReadyRewad`,`FinalRewad`,`ReadyTurn`,`FinalTurn`,");
		sqlBuffer.append(" `ReadyWinCount`,`FinalWinCount`,`ReadyTakeTime`,`FinalTakeTime`,`IsExist`,");
		sqlBuffer.append(" `CreateDate`,`Property1`,`Property2`,`LordsIndex`,`FightPowerOrder`,TurnList,Jobs FROM %s A WHERE A.`ReadyOrder` > 0 AND A.`ReadyOrder` <=16 ORDER BY A.`FinalWinCount` DESC,A.`FightPower` DESC) B");
		sqlBuffer.append(" UNION ALL");
		sqlBuffer.append(" SELECT * FROM(SELECT `Uuid`,`TeamUid`,`UserId`,`ServerName`,`NickName`,`UserKeys`,`Job`,`SexJob`,`Grades`,`FashionArm`,`FashionWing`,`FashionCloth`,`FashionHair`,");
		sqlBuffer.append(" `FashionHat`,`Arm`,`IsVip`,`FightPower`,`ReadyScore`,`ReadyOrder`,`FinalScore`,`FinalOrder`,`ReadyRewad`,`FinalRewad`,`ReadyTurn`,`FinalTurn`,");
		sqlBuffer.append(" `ReadyWinCount`,`FinalWinCount`,`ReadyTakeTime`,`FinalTakeTime`,`IsExist`,");
		sqlBuffer.append(" `CreateDate`,`Property1`,`Property2`,`LordsIndex`,`FightPowerOrder`,TurnList,Jobs FROM %s C WHERE C.`ReadyOrder` > 16 AND C.`ReadyOrder`<= 100 ORDER BY C.`FinalWinCount` DESC,C.`FightPower` DESC) D;");

//		String sql = "SELECT * FROM `%s` ORDER BY FinalWinCount DESC,FightPower DESC LIMIT %s;";
		String sql = sqlBuffer.toString();
		sql = String.format(sql, "t_u_cross_multilords","t_u_cross_multilords");
		System.out.println(sql);
	}
}