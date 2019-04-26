//package inext;
//
//import java.io.IOException;
//
//import javax.annotation.Resource;
//
//import org.bouncycastle.crypto.CryptoException;
//import org.jibx.runtime.JiBXException;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.inext.Application;
//import com.inext.dao.IAssetBorrowOrderDao;
//import com.inext.service.CreditService;
//import com.inext.service.IAssetBorrowOrderService;
//import com.inext.service.IAssetRepaymentOrderService;
//import com.inext.service.ILianLianPayService;
//import com.inext.service.kq.webservice.KqPaySevice;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//public class Test {
//
//    @Resource
//    CreditService creditService;
//    @Resource
//    ILianLianPayService lianLianPayService;
//    @Resource
//    IAssetBorrowOrderDao assetBorrowOrderDao;
//    @Resource
//    private IAssetBorrowOrderService assetBorrowOrderService;
//    @Resource
//    private IAssetRepaymentOrderService assetRepaymentOrderService;
//    @Resource
//    private KqPaySevice kqPaySevice;
//    /*@org.junit.Test
//    public void payForAnother() {
//        HashMap<String, Object> paramsM = new HashMap<String, Object>();
//        paramsM.put("status", AssetBorrowOrder.STATUS_DFK);
//        paramsM.put("payStatus", AssetBorrowOrder.SUB_PAY_CSZT);
//        paramsM.put("querylimit", 28);
//        List<AssetBorrowOrder> bos = assetBorrowOrderService.findAll(paramsM);
//        if (bos != null && bos.size() > 0) {
//            //请求打款
//            try {
//                assetBorrowOrderService.payForAnother(bos);
//            } catch (Exception e) {
//            }
//        }
//    }
//    @org.junit.Test
//    public void commonTest() {
//
//        //放款中的状态全部查询 28
//        HashMap<String, Object> paramsM = new HashMap<String, Object>();
//        paramsM.put("status", AssetBorrowOrder.STATUS_FKZ);
//        paramsM.put("querylimit", 28);
//        List<AssetBorrowOrder> bos = assetBorrowOrderService.findAll(paramsM);
//        if (bos != null && bos.size() > 0) {
//            try {
//                assetBorrowOrderService.queryPayForAnother(bos);
//            } catch (Exception e) {
//            }
//        }
//
//    }
//
//    *//**
//     * 连连查询代付结果
//     *//*
//    @org.junit.Test
//    public void queryPayLianLian(){
//
//        AssetBorrowOrder assetBorrowOrder = assetBorrowOrderDao.getOrderById(68);
//        ApiServiceResult serviceResult = lianLianPayService.queryPaymentApi(assetBorrowOrder);
//        System.out.println(serviceResult.getCode());
//        System.out.println(serviceResult.getMsg());
//
//    }*/
//
//    @org.junit.Test
//    public void payForAnother() throws JiBXException, IOException, CryptoException {
//        /*Map<String, Object> map = new HashMap<>();
//        map.put("repaymentTime", new Date());
//        List<AssetRepaymentOrderByWithholdDto> list = assetRepaymentOrderService.getListByWithhold(map);
//        System.out.println(JSON.toJSONString(list));*/
//        //ppdTask ppdTask = new ppdTask();
//        //ppdTask.testAdvDebitBatch();
//
//        //kqPaySevice.advDebitBatch();
//        kqPaySevice.pkiBatchQuery();
//    }
//
//}
