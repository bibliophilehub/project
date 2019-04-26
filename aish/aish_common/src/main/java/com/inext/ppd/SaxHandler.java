package com.inext.ppd;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;
import java.util.Properties;

/**
 * 招行XML报文解析类
 * @author shensiping
 */
public class SaxHandler extends DefaultHandler {
	private int layer=0;
	private String curSectionName;
	private String curKey;
	private String curValue;
	private XmlPacket pktData;
	private Map mpRecord;

	public SaxHandler(XmlPacket data){
		curSectionName = "";
		curKey = "";
		curValue = "";
		pktData = data;
		mpRecord = new Properties();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		layer++;
		if(layer == 2) {
			curSectionName = qName;
			curKey = qName;
		}else if(layer == 3) {
			curKey = qName;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if(layer==2){
			if(!"tns:items".equals(curSectionName)){
				pktData.putEntity(curKey, curValue);
				if("tns:dealResult".equals(curKey)){
					pktData.setRETCOD(curValue);
				}
			}else{
				pktData.putProperty(curSectionName, mpRecord);
				mpRecord = new Properties();
			}

		}else if(layer==3){
			mpRecord.put(curKey, curValue);
		}
		curValue = "";
		layer--;
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		if(layer==3 || layer==2){
			String value = new String(ch, start, length);
			if("\n".equals(ch)){
				curValue += "\r\n";
			}else{
				curValue += value;
			}
		}
	}
}
