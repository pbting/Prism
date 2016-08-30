package exetuor;

import java.util.ArrayList;
import java.util.List;

public class SubListTest {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("pbting-1");
		list.add("pbting-2");
		list.add("pbting-3");
		list.add("pbting-4");
		list.add("pbting-5");
		System.out.println("src size:"+list.size());
		List<String> subList = list.subList(0, list.size());
		System.out.println(subList.size());
		String sqlText = "SELECT * FROM `t_u_sumactivecondition` A WHERE A.`ConditionId` = %s AND DATE(A.`CurrentDate`)='%s';";
		sqlText = String.format(sqlText, 2061,"2017-03-19");
		System.out.println(sqlText);
	}
}
