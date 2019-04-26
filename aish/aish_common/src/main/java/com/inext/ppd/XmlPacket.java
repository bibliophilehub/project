package com.inext.ppd;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.util.*;

/**
 *
 * 招行XML通讯报文类
 * @author shensiping
 */
@Data
public class XmlPacket {


	protected String FUNNAM;
	/**
	 * 报文类型固定为2
	 */
	protected final String DATTYP= "2";
	protected String LGNNAM;
	protected String RETCOD;
	protected String ERRMSG;


	/**
	 * <String,Vector>
	 */
	protected Map data;
	protected Map entity;

	public XmlPacket(){
		data = new Properties();
		entity = new HashMap();
	}

	public XmlPacket(String sFUNNAM){
		FUNNAM = sFUNNAM;
		data = new Properties();
	}

	public XmlPacket(String sFUNNAM, String sLGNNAM){
		FUNNAM = sFUNNAM;
		LGNNAM = sLGNNAM;
		data = new Properties();
	}


	/**
	 * XML报文返回头中内容是否表示成功
	 * @return
	 */
	public boolean isError(){
		return !(ReturnCode.SUCCESS.getCode().equals(RETCOD) ||
				ReturnCode.PPD_SUCCESS.getCode().equals(RETCOD) ||
				ReturnCode.PPD_WAIT.getCode().equals(RETCOD));
	}

	/**
	 * 插入数据记录
	 * @param sSectionName
	 * @param mpData <String, String>
	 */
	public void putProperty(String sSectionName, Map mpData){
		if(data.containsKey(sSectionName)){
			Vector vt = (Vector)data.get(sSectionName);
			vt.add(mpData);
		}else{
			Vector vt = new Vector();
			vt.add(mpData);
			data.put(sSectionName, vt);
		}
	}

	public void putEntity(String key, String value){
		entity.put(key,value);
	}

	/**
	 * 取得指定接口的数据记录
	 * @param sSectionName
	 * @param index 索引，从0开始
	 * @return Map<String,String>
	 */
	public Map getProperty(String sSectionName, int index){
		if(data.containsKey(sSectionName)){
			return (Map)((Vector)data.get(sSectionName)).get(index);
		}else{
			return null;
		}
	}

	/**
	 * 取得制定接口数据记录数
	 * @param sSectionName
	 * @return
	 */
	public int getSectionSize(String sSectionName){
		if(data.containsKey(sSectionName)){
			Vector sec = (Vector)data.get(sSectionName);
			return sec.size();
		}
		return 0;
	}

	/**
	 * 取得制定接口数据记录列表
	 * @param sSectionName
	 * @return
	 */
	public Vector getSectionList(String sSectionName){
		if(data.containsKey(sSectionName)){
			Vector sec = (Vector)data.get(sSectionName);
			return sec;
		}
		return null;
	}

	/**
	 * 把报文转换成XML字符串
	 * @return
	 */
	public String toXmlString(){
		StringBuffer sfData = new StringBuffer("<?xml version='1.0' encoding = 'GBK'?>");
		sfData.append("<CMBSDKPGK>");
		sfData.append("<INFO><FUNNAM>"+FUNNAM+"</FUNNAM><DATTYP>"+DATTYP+"</DATTYP><LGNNAM>"+LGNNAM+"</LGNNAM></INFO>");
		int secSize = data.size();
		Iterator itr = data.keySet().iterator();
		while(itr.hasNext()){
			String secName = (String)itr.next();
			Vector vt = (Vector)data.get(secName);
			for(int i=0; i<vt.size(); i++){
				Map record = (Map)vt.get(i);
				Iterator itr2 = record.keySet().iterator();
				sfData.append("<"+secName+">");
				while(itr2.hasNext()){
					String datakey = (String)itr2.next();
					String dataValue = (String)record.get(datakey);
					sfData.append("<"+datakey+">");
					sfData.append(dataValue);
					sfData.append("</"+datakey+">");
				}
				sfData.append("</"+secName+">");
			}
		}
		sfData.append("</CMBSDKPGK>");
		return sfData.toString();
	}

	/**
	 * 解析xml字符串，并转换为报文对象
	 * @param message
	 */
	public static XmlPacket valueOf(String message) {
		try {
			SAXParserFactory saxfac = SAXParserFactory.newInstance();
			SAXParser saxparser = saxfac.newSAXParser();
			ByteArrayInputStream is = new ByteArrayInputStream(message.getBytes("utf-8"));
			XmlPacket xmlPkt= new XmlPacket();
			saxparser.parse(is, new SaxHandler(xmlPkt));
			is.close();
			return xmlPkt;
		} catch (Exception e) {
			System.out.println("解析xml字符串出错：e = "+e.getMessage());
			return new XmlPacket();
		}
	}

	public Map<String,Object> resultBatch(Map map) {
		Map<String, Object> m = new HashMap<>();
		if (map != null) {
			m.put("batchId", map.get("tns:batchId"));
			m.put("batchErrMessage", map.get("tns:batchErrMessage"));
			m.put("batchErr", map.get("tns:batchErr"));
			m.put("ext1", map.get("tns:ext1"));
			m.put("ext2", map.get("tns:ext2"));
			m.put("requestId", map.get("tns:requestId"));
			m.put("contractId", map.get("tns:contractId"));
			m.put("memberCode", map.get("tns:memberCode"));
			m.put("merchantAcctId", map.get("tns:merchantAcctId"));
			m.put("receiveTime", map.get("tns:receiveTime"));
			m.put("requestTime", map.get("tns:requestTime"));
			m.put("numTotal", map.get("tns:numTotal"));
			m.put("amountTotal", map.get("tns:amountTotal"));
			m.put("dealResult", map.get("tns:dealResult"));
		}
		System.out.println(JSON.toJSONString(m));
		return m;
	}

	public List<PpdDebitItem> resultItems(Map data) {
		Vector<Map<String, String>> vector = (Vector<Map<String, String>>) data.get("tns:items");
		List<PpdDebitItem> list = new ArrayList<PpdDebitItem>();
		if (CollectionUtils.isNotEmpty(vector)) {
			for (Map<String, String> map : vector) {
				PpdDebitItem m = new PpdDebitItem();
				m.setSeqId(map.get("tns:seqId"));
				m.setSeqNumber(map.get("tns:seqNumber"));
				m.setBankAcctId(map.get("tns:bankAcctId"));
				m.setBankAcctName(map.get("tns:bankAcctName"));
				m.setAmount(map.get("tns:amount"));
				m.setRemark(map.get("tns:remark"));
				m.setDealResult(map.get("tns:dealResult"));
				m.setErrMessage(map.get("tns:errMessage"));
				list.add(m);
			}
		}
		System.out.println(JSON.toJSONString(list));
		return list;
	}

	public Map<String,Object> resultM(Map map) {
		Map<String, Object> m = new HashMap<>();
		if (map != null) {
			m.put("batchId", map.get("tns:batchId"));
			m.put("errCode", map.get("tns:errCode"));
			m.put("errMessage", map.get("tns:errMessage"));
			m.put("ext1", map.get("tns:ext1"));
			m.put("ext2", map.get("tns:ext2"));
			m.put("failAmount", map.get("tns:failAmount"));
			m.put("failNum", map.get("tns:failNum"));
			m.put("memberCode", map.get("tns:memberCode"));
			m.put("merchantAcctId", map.get("tns:merchantAcctId"));
			m.put("receiveTime", map.get("tns:receiveTime"));
			m.put("requestTime", map.get("tns:requestTime"));
			m.put("successAmount", map.get("tns:successAmount"));
			m.put("successNum", map.get("tns:successNum"));
			m.put("batchResult", map.get("tns:batchResult"));

		}
		System.out.println(JSON.toJSONString(m));
		return m;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public Map getEntity() {
		return entity;
	}

	public void setEntity(Map entity) {
		this.entity = entity;
	}

	public String getRETCOD() {
		return RETCOD;
	}

	public void setRETCOD(String RETCOD) {
		this.RETCOD = RETCOD;
	}
}