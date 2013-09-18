package com.guyiping;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetBusInfo {

	/**
	 * @param args
	 */
	HashMap<String, BusInfo> BusInfoMap = null;

	public GetBusInfo() {
		// TODO Auto-generated constructor stub
		BusInfoMap = new HashMap<String, BusInfo>();
	}

	private static GetBusInfo instance;

	public static synchronized GetBusInfo getInstance() {
		if (instance == null) {
			instance = new GetBusInfo();
		}
		return instance;
	}

	public boolean isWebSiteNormal() throws Exception {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				50000);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.hzbus.cn")
				.setPath("/Page/get3GPS.aspx").setParameter("zxc", "0")
				.setParameter("sid", "0.04432708525338469");
		URI busLineUrl = builder.build();
		HttpGet httpGet = new HttpGet(busLineUrl);

		try {
			HttpResponse response1 = httpclient.execute(httpGet);
			String charset = "GB2312";
			System.out.println(response1.getStatusLine());
			HttpEntity webStatus = response1.getEntity();
			if (EntityUtils.toString(webStatus, charset).equals("1")) {
				return true;
			} else {
				return false;
			}
			// System.out.println(EntityUtils.toString(entity1, charset));
			// Document doc =
			// Jsoup.parse(EntityUtils.toString(entity1,charset));

			// do something useful with the response body
			// and ensure it is fully consumed
			// EntityUtils.consume(entity1);

		} catch (Exception e) {
			return false;
		} finally {
			httpGet.releaseConnection();
		}

	}

	public boolean isQueryNormal() {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				50000);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.hzbus.cn")
				.setPath("/Page/get3GPS.aspx").setParameter("zxc", "0")
				.setParameter("sid", "0.04432708525338469");

		HttpGet httpGet = null;
		try {
			URI busLineUrl = builder.build();
			httpGet = new HttpGet(busLineUrl);
			HttpResponse response1 = httpclient.execute(httpGet);
			// System.out.println(response1.getStatusLine());
			return true;

		} catch (Exception e) {
			return false;

		} finally {
			httpGet.releaseConnection();
		}

	}

	public String setLineInfo(String busno) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				50000);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.hzbus.cn")
				.setPath("/Page/LineSearch.aspx")
				.setParameter("flinename", busno).setParameter("rnd", "2");
		HttpGet httpGet = null;
		try {
			URI busLineUrl = builder.build();
			httpGet = new HttpGet(busLineUrl);
			HttpResponse response1 = httpclient.execute(httpGet);
			String charset = "GB2312";
			// System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			// System.out.println(EntityUtils.toString(entity1, charset));
			Document doc = Jsoup.parse(EntityUtils.toString(entity1, charset));
			Elements e = doc.getElementsByClass("bt");

			BusInfo busInfo = new BusInfo(busno, "0");
			for (int i = 0; i < e.size(); i++) {
				// 获取公交车方向
				Element ee = e.get(i);
				String lineDirection = ee.text();

				// 新建个路线对象
				LineInfo lineInfo = new LineInfo(lineDirection);

				// 获取站台信息Stop name，x，y
				String[] p = ee.toString().split("Stop name=");
				// System.out.println(p[1]);

				for (int j = 1; j < p.length; j++) {
					String[] sTemp = p[j].split("&quot;");
					String stationName = sTemp[1];
					String x = sTemp[3];
					String y = sTemp[5];
					StationInfo si = new StationInfo(stationName, j, x, y);
					lineInfo.addStationInfo(si);
				}

				busInfo.addLineInfo(lineInfo);
			}

			// 获取公交车内部ID
			e = doc.select("li#bizName>input#RepeaterLine_ctl00_cur_lineid_txt");
			Element ee = e.get(0);
			busInfo.setInternalNo(ee.attr("value"));

			// 将businfo放入businfo map中
			BusInfoMap.put(busno, busInfo);
			System.out.println("Success to get " + busno + " info.");

			return busInfo.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			httpGet.releaseConnection();
		}
	}

	public String getLineInfo(String busno) {

		if (BusInfoMap.containsKey(busno)) {
			BusInfo busInfo = BusInfoMap.get(busno);
			return busInfo.toString();
		} else if (isQueryNormal()) {
			return setLineInfo(busno);
		} else {
			return "hzbus.cn网站业务暂停，请稍后再试！";
		}

	}

	public String getGPSInfo(String busno, String stationInfo) {
		BusInfo busInfo = BusInfoMap.get(busno);
		if (busInfo == null) {
			return null;
		}

		StringBuffer gpsInfo = new StringBuffer();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				50000);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);

		for (int i = 0; i < busInfo.lineInfo.size(); i++) {
			gpsInfo.append(busInfo.lineInfo.get(i).lineDirection).append(":");
			for (int j = 0; j < busInfo.lineInfo.get(i).stationInfo.size(); j++) {
				String stationName = busInfo.lineInfo.get(i).stationInfo.get(j).StationName;
				if (stationName.contains(stationInfo)) {
					URIBuilder builder = new URIBuilder();
					builder.setScheme("http")
							.setHost("www.hzbus.cn")
							.setPath("/page/get3GPS.aspx")
							.setParameter("fuid", busInfo.internalNo)
							.setParameter("type", "1")
							.setParameter("rnd", "3")
							.setParameter(
									"x",
									busInfo.lineInfo.get(i).stationInfo.get(j).x_station)
							.setParameter(
									"y",
									busInfo.lineInfo.get(i).stationInfo.get(j).y_station)
							.setParameter("sid", "0.5236836370110548");
					HttpGet httpGet = null;
					try {
						URI busLineUrl = builder.build();
						httpGet = new HttpGet(busLineUrl);
						HttpResponse response1 = httpclient.execute(httpGet);
						String charset = "GB2312";
						// System.out.println(response1.getStatusLine());
						HttpEntity entity1 = response1.getEntity();
						// System.out.println(EntityUtils.toString(entity1,
						// charset));
						Document doc = Jsoup.parse(EntityUtils.toString(
								entity1, charset));
						String stemp = doc.getElementsByTag("body").get(0).childNode(0).toString();
						stemp = stemp.replaceAll("\n", "");
						gpsInfo.append(busInfo.lineInfo.get(i).stationInfo.get(j).StationName).append(":").append(stemp).append("\n");
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} finally {
						httpGet.releaseConnection();
					}

				}

			}

		}
		
		return gpsInfo.toString();

	}

	public String autoConfirmBusNo(String UserInputContent) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				50000);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.hzbus.cn").setPath("/iLine.axd")
				.setParameter("n", UserInputContent)
				.setParameter("rnd", "8811");
		URI busLineUrl = builder.build();
		HttpGet httpGet = new HttpGet(busLineUrl);
		HttpResponse response1 = httpclient.execute(httpGet);
		try {
			String charset = "GB2312";
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			// System.out.println(EntityUtils.toString(entity1, charset));
			Document doc = Jsoup.parse(EntityUtils.toString(entity1, charset));
			Elements e = doc.getElementsByTag("row");

			if (e.size() > 1) {
				StringBuffer sb = new StringBuffer();
				sb.append("哎呀，小助手查出好多好多辆车啊！\n");
				for (int i = 0; i < e.size(); i++) {
					String busno = e.get(i).toString().split("\"")[1];

					if (busno.length() == UserInputContent.length()) {
						return busno;
					}
					if (0 == busno.substring(UserInputContent.length(),
							UserInputContent.length() + 1).compareTo("/")) {
						return busno;
					}

					sb.append(i + 1).append(".").append(busno).append("\n");
				}
				sb.append("到底是哪一辆呢？");
				return sb.toString();

			} else if (e.size() == 1) {
				String busno = e.get(0).toString().split("\"")[1];
				return busno;
			} else {
				return "~~~~(>_<)~~~~ ，怎么没有这辆车，是不是输错了，直接输入数字或者英文字符即可，例如55路，只需要输入55，\n网站数据是实时获取，一次不行请一直猛戳。";
			}

		} finally {
			httpGet.releaseConnection();
		}
	}

	public void autoGetBusInfo(String input) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		URIBuilder builder = new URIBuilder();

		builder.setScheme("http").setHost("www.hzbus.cn").setPath("/iLine.axd")
				.setParameter("n", input).setParameter("rnd", "8811");
		HttpGet httpGet = null;
		try {
			URI busLineUrl = builder.build();
			httpGet = new HttpGet(busLineUrl);
			HttpResponse response1 = httpclient.execute(httpGet);
			String charset = "GB2312";
			HttpEntity entity1 = response1.getEntity();
			// System.out.println(EntityUtils.toString(entity1, charset));
			Document doc = Jsoup.parse(EntityUtils.toString(entity1, charset));
			Elements e = doc.getElementsByTag("row");
			for (int j = 0; j < e.size(); j++) {
				String busno = e.get(j).toString().split("\"")[1];
				GetBusInfo.getInstance().getLineInfo(busno);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
	}

	/*
	 * 
	 * public static void main(String[] args) throws Exception { // TODO
	 * Auto-generated method stub DefaultHttpClient httpclient = new
	 * DefaultHttpClient(); URIBuilder builder = new URIBuilder();
	 * builder.setScheme
	 * ("http").setHost("www.hzbus.cn").setPath("/Page/LineSearch.aspx")
	 * .setParameter("flinename", "B支1线").setParameter("rnd", "2"); URI
	 * busLineUrl = builder.build(); HttpGet httpGet = new HttpGet(busLineUrl);
	 * HttpResponse response1 = httpclient.execute(httpGet);
	 * 
	 * try { String charset = "GB2312";
	 * System.out.println(response1.getStatusLine()); HttpEntity entity1 =
	 * response1.getEntity(); System.out.println(EntityUtils.toString(entity1,
	 * charset)); Document doc =
	 * Jsoup.parse(EntityUtils.toString(entity1,charset));
	 * 
	 * // do something useful with the response body // and ensure it is fully
	 * consumed EntityUtils.consume(entity1);
	 * 
	 * } finally { httpGet.releaseConnection(); }
	 * 
	 * }
	 */

}
