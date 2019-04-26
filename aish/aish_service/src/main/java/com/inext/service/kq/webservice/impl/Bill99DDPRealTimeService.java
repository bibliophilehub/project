package com.inext.service.kq.webservice.impl;

import com.bill99.asap.exception.CryptoException;
import com.bill99.asap.service.ICryptoService;
import com.bill99.asap.service.impl.CryptoServiceFactory;
import com.bill99.schema.asap.commons.Mpf;
import com.bill99.schema.asap.data.SealedData;
import com.bill99.schema.asap.data.UnsealedData;
import com.bill99.schema.asap.message.SealRequestBody;
import com.bill99.schema.commons.Version;
import com.bill99.schema.ddp.product.*;
import com.bill99.schema.ddp.product.head.MerchantDebitHead;
import com.bill99.schema.ddp.product.pki.SealDataType;
import com.bill99.seashell.common.util.Base64Util;
import com.inext.exception.BusinessExceptionFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class Bill99DDPRealTimeService {

    private static Logger LOGGER = LoggerFactory.getLogger(Bill99DDPRealTimeService.class);

    public String ENCODING = "utf-8";
    public final String FEATURECODE = "F168_5";
    private WebServiceTemplate sealWebServiceTemplate;

    public WebServiceTemplate getSealWebServiceTemplate() {
        return sealWebServiceTemplate;
    }

    public void setSealWebServiceTemplate(WebServiceTemplate sealWebServiceTemplate) {
        this.sealWebServiceTemplate = sealWebServiceTemplate;
    }

    /**
     * 加密 发送 解密
     *
     * @param memberCode
     * @param originalData
     * @param head
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    private String sealAndSend(String memberCode, String originalData, MerchantDebitHead head) throws JiBXException, IOException, CryptoException {
        MerchantDebitPkiRequest pkiReq = new MerchantDebitPkiRequest();
        pkiReq.setHead(head);

        MerchantDebitPkiRequestBody body = new MerchantDebitPkiRequestBody();
        SealDataType sealdata = new SealDataType();
        SealedData sealedData = null;
        SealRequestBody srb = new SealRequestBody();
        srb.setOriginalData(originalData.getBytes(ENCODING));
        Mpf mpf = new Mpf();
        mpf.setMemberCode(memberCode);
        mpf.setFeatureCode(FEATURECODE);
        ICryptoService service = CryptoServiceFactory.createCryptoService();

        sealedData = service.seal(mpf, originalData.getBytes(ENCODING));

        byte[] byteOri = new byte[0];
        byte[] byteEnc = new byte[0];
        byte[] byteEnv = new byte[0];
        byte[] byteSig = new byte[0];
        if (null != sealedData.getOriginalData())
            byteOri = sealedData.getOriginalData();
        if (null != sealedData.getEncryptedData())
            byteEnc = sealedData.getEncryptedData();
        if (null != sealedData.getDigitalEnvelope())
            byteEnv = sealedData.getDigitalEnvelope();
        if (null != sealedData.getSignedData())
            byteSig = sealedData.getSignedData();

        byte[] byteOri2 = base64Gzip(byteOri);
        byte[] byteEnc2 = base64Gzip(byteEnc);
        byte[] byteEnv2 = base64Gzip(byteEnv);
        byte[] byteSig2 = base64Gzip(byteSig);

        sealdata.setOriginalData("");
        sealdata.setEncryptedData(new String(byteEnc2, ENCODING));
        sealdata.setDigitalEnvelope(new String(byteEnv2, ENCODING));
        sealdata.setSignedData(new String(byteSig2, ENCODING));

        body.setMemberCode(memberCode);
        body.setData(sealdata);
        pkiReq.setBody(body);

        Object obj = sealWebServiceTemplate.marshalSendAndReceive(pkiReq);
        MerchantDebitPkiResponse response = (MerchantDebitPkiResponse) obj;


        SealDataType sdt = response.getBody().getData();
        SealedData sd = new SealedData();

        byteOri = new byte[0];
        byteEnc = new byte[0];
        byteEnv = new byte[0];
        byteSig = new byte[0];
        if (null != sdt.getOriginalData())
            byteOri = sdt.getOriginalData().getBytes(ENCODING);
        if (null != sdt.getEncryptedData())
            byteEnc = sdt.getEncryptedData().getBytes(ENCODING);
        if (null != sdt.getDigitalEnvelope())
            byteEnv = sdt.getDigitalEnvelope().getBytes(ENCODING);
        if (null != sdt.getSignedData())
            byteSig = sdt.getSignedData().getBytes(ENCODING);

        byteOri2 = base64Ungzip(byteOri);
        byteEnc2 = base64Ungzip(byteEnc);
        byteEnv2 = base64Ungzip(byteEnv);
        byteSig2 = base64Ungzip(byteSig);

        sd.setOriginalData(byteOri2);
        sd.setEncryptedData(byteEnc2);
        sd.setDigitalEnvelope(byteEnv2);
        sd.setSignedData(byteSig2);

        mpf = new Mpf();
        mpf.setMemberCode(memberCode);
        mpf.setFeatureCode(FEATURECODE);

        service = CryptoServiceFactory.createCryptoService();

        UnsealedData usd = service.unseal(mpf, sd);

        return new String(usd.getDecryptedData(), ENCODING);
    }

    /**
     * 免签约单笔交易请求
     *
     * @param singleRequest
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String advSingleDebit(MerchantSingleDebitRequest singleRequest) throws JiBXException, IOException, CryptoException {
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debit.single");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        IBindingFactory bfact = BindingDirectory.getFactory(MerchantSingleDebitRequest.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        //mctx.setIndent(2);
        sw = new StringWriter();
        mctx.setOutput(sw);
        mctx.marshalDocument(singleRequest);
        String originalData = sw == null ? "" : sw.toString();

        String res = sealAndSend(singleRequest.getMemberCode(), originalData, head);
        System.out.println("免签约单笔请求" + sw);
        System.out.println("免签约单笔返回" + res);
        return res;
    }

    /**
     * 协议代收单笔交易请求
     *
     * @param singleRequest
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String signSingleDebit(MerchantProtocalSingleDebitRequest singleRequest) throws JiBXException, IOException, CryptoException {
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debit.sign.single");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        IBindingFactory bfact = BindingDirectory.getFactory(MerchantProtocalSingleDebitRequest.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        //mctx.setIndent(2);
        sw = new StringWriter();
        mctx.setOutput(sw);
        mctx.marshalDocument(singleRequest);
        String originalData = sw == null ? "" : sw.toString();

        String res = sealAndSend(singleRequest.getMemberCode(), originalData, head);
        System.out.println("协议代收单笔请求" + sw);
        System.out.println("协议代收单笔返回" + res);
        return res;
    }

    /**
     * 免签约批量交易请求
     *
     * @param request
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String advDebitBatch(MerchantDebitRequest request) {
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debit");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        String res = null;
        try {
            IBindingFactory bfact = BindingDirectory.getFactory(MerchantDebitRequest.class);
            IMarshallingContext mctx = bfact.createMarshallingContext();
            //mctx.setIndent(2);
            sw = new StringWriter();
            mctx.setOutput(sw);
            mctx.marshalDocument(request);
            String originalData = sw == null ? "" : sw.toString();
            LOGGER.info("Bill99DDPRealTimeService  >>> advDebitBatch 免签约批量交易请求:{}", sw);
            res = sealAndSend(request.getMemberCode(), originalData, head);
            LOGGER.info("Bill99DDPRealTimeService  >>> advDebitBatch 免签约批量交易返回:{}", res);
        } catch (JiBXException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求JiBXException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求IOException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (CryptoException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求CryptoException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求Exception异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        }
        return res;
    }

    /**
     * 协议批量交易请求
     *
     * @param request
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String signDebitBatch(MerchantProtocalDebitRequest request) throws JiBXException, IOException, CryptoException {
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debit.sign.batch");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        IBindingFactory bfact = BindingDirectory.getFactory(MerchantProtocalDebitRequest.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        //mctx.setIndent(2);
        sw = new StringWriter();
        mctx.setOutput(sw);
        mctx.marshalDocument(request);
        String originalData = sw == null ? "" : sw.toString();

        String res = sealAndSend(request.getMemberCode(), originalData, head);
        System.out.println("协议批量交易请求" + sw);
        System.out.println("协议批量交易返回" + res);
        return res;
    }

    /**
     * 协议查询
     *
     * @param request
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String merchantContractQuery(MerchantProtocalQueryRequest request) throws JiBXException, IOException, CryptoException {
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debit.sign.batch");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        IBindingFactory bfact = BindingDirectory.getFactory(MerchantProtocalDebitRequest.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        //mctx.setIndent(2);
        sw = new StringWriter();
        mctx.setOutput(sw);
        mctx.marshalDocument(request);
        String originalData = sw == null ? "" : sw.toString();

        String res = sealAndSend(request.getMemberCode(), originalData, head);
        System.out.println("协议查询请求" + sw);
        System.out.println("协议查询返回" + res);
        return res;
    }

    /**
     * 查询交易明细
     *
     * @param req
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String query(MerchantDebitQueryRequest req){
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debitquery");
        version.setVersion("1");
        head.setVersion(version);
        StringWriter sw = null;
        String res = null;
        try {
            IBindingFactory bfact = BindingDirectory.getFactory(MerchantDebitQueryRequest.class);
            IMarshallingContext mctx = bfact.createMarshallingContext();
            //mctx.setIndent(2);
            sw = new StringWriter();
            mctx.setOutput(sw);
            mctx.marshalDocument(req);
            String originalData = sw == null ? "" : sw.toString();
            LOGGER.info("Bill99DDPRealTimeService  >>> query 查询交易明细请求:{}", sw);
            res = sealAndSend(req.getMemberCode(), originalData, head);
            LOGGER.info("Bill99DDPRealTimeService  >>> query 查询交易明细返回:{}", res);

        } catch (JiBXException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求JiBXException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求IOException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (CryptoException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求CryptoException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求Exception异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        }
        return res;
    }

    /**
     * 查询单笔交易
     *
     * @param req
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String signleQuery(MerchantDebitSingleQueryRequest req) throws JiBXException, IOException, CryptoException {
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debitsinglequery");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        IBindingFactory bfact = BindingDirectory.getFactory(MerchantDebitQueryRequest.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        //mctx.setIndent(2);
        sw = new StringWriter();
        mctx.setOutput(sw);
        mctx.marshalDocument(req);
        String originalData = sw == null ? "" : sw.toString();
        String res = sealAndSend(req.getMemberCode(), originalData, head);
        System.out.println("查询交易明细请求" + sw);
        System.out.println("查询交易明细返回" + res);
        return res;
    }

    /**
     * 查询交易批次
     *
     * @param req
     * @return
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public String batchQuery(MerchantDebitBatchQueryRequest req){
        MerchantDebitHead head = new MerchantDebitHead();
        Version version = new Version();
        version.setService("ddp.product.debitbatchquery");
        version.setVersion("1");
        head.setVersion(version);

        StringWriter sw = null;
        String res = null;
        try {
            IBindingFactory bfact = BindingDirectory.getFactory(MerchantDebitBatchQueryRequest.class);
            IMarshallingContext mctx = bfact.createMarshallingContext();
            //mctx.setIndent(2);
            sw = new StringWriter();
            mctx.setOutput(sw);
            mctx.marshalDocument(req);
            String originalData = sw == null ? "" : sw.toString();
            LOGGER.info("Bill99DDPRealTimeService  >>> batchQuery 查询交易批次请求:{}", sw);
            res = sealAndSend(req.getMemberCode(), originalData, head);
            LOGGER.info("Bill99DDPRealTimeService  >>> batchQuery 查询交易批次返回:{}", res);
        } catch (JiBXException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求JiBXException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求IOException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (CryptoException e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求CryptoException异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error("Bill99DDPRealTimeService >>> advDebitBatch 批量交易请求Exception异常:{}", e.getMessage());
            throw BusinessExceptionFactory.create(ExceptionUtils.getStackTrace(e));
        }
        return res;
    }

    private byte[] base64Gzip(byte[] b) throws IOException {
        if (null == b)
            return new byte[0];

        byte[] bytes01 = gzip(b); // GzipUtil.gzip(ori, "UTF-8");
        byte[] bytes02 = Base64Util.encode(bytes01);
        return bytes02;
    }

    public byte[] gzip(byte[] b1) {
        byte[] b = null;
        ByteArrayOutputStream bo = null;
        GZIPOutputStream gzipo = null;
        try {
            bo = new ByteArrayOutputStream();
            gzipo = new GZIPOutputStream(bo);
            gzipo.write(b1);
            gzipo.finish();
            b = bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (gzipo != null)
                    gzipo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bo != null)
                    bo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    private static byte[] base64Ungzip(byte[] b) throws IOException {
        byte[] b2 = Base64Util.decode(b);
        byte[] b3 = unBGzip(b2);
        return b3;
    }

    public static byte[] unBGzip(byte[] b) {
        if (b == null || b.length == 0)
            return "".getBytes();
        byte[] retb = null;
        ByteArrayOutputStream bo = null;
        ByteArrayInputStream bi = null;
        GZIPInputStream gzipi = null;
        byte[] bBuf = new byte[4096];
        try {
            bo = new ByteArrayOutputStream();
            bi = new ByteArrayInputStream(b);
            gzipi = new GZIPInputStream(bi);
            int i = 0;
            while ((i = gzipi.read(bBuf)) != -1) {
                bo.write(bBuf, 0, i);
            }
            retb = bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (gzipi != null)
                    gzipi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bi != null)
                    bi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bo != null)
                    bo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retb;
    }
}
