//package inext;
//
//import com.inext.Application;
//import com.inext.dao.IAssetOrderStatusHisDao;
//import com.inext.entity.AssetBorrowOrder;
//import com.inext.entity.AssetOrderStatusHis;
//import com.inext.service.CreditService;
//import com.inext.service.IAssetBorrowOrderService;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//public class Test {
//
//    @Resource
//    CreditService creditService;
//    @Resource
//    private IAssetOrderStatusHisDao iAssetOrderStatusHisDao;
//    @Resource
//    private IAssetBorrowOrderService assetBorrowOrderService;
//
//    @org.junit.Test
//    public void commonTest() {
//
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
//                System.err.println("xxoo"+e);
//            }
//        }
//    }
//    
//    
//    @org.junit.Test
//    public void hisTest() {
//    	AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
//		assetOrderStatusHis.setOrderId(31);
//		assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_DSH);
////		int count=		iAssetOrderStatusHisDao.selectCount(assetOrderStatusHis);
//		List<AssetOrderStatusHis> list=iAssetOrderStatusHisDao.select(assetOrderStatusHis);
//		System.out.println(list);
//		System.out.println(list==null);
//		System.out.println(list.size());
//    }
//    @org.junit.Test
//    public void his2Test() {
//    	AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
//    	assetOrderStatusHis.setId(1);
//    	assetOrderStatusHis.setOrderId(31);
//    	assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_DSH);
//    	assetOrderStatusHis.setCreateTime(new Date());
//    	assetOrderStatusHis.setRemark("审核中a");
//    	
//    	iAssetOrderStatusHisDao.updateByPrimaryKey(assetOrderStatusHis);
////    	iAssetOrderStatusHisDao.updateByExampleSelective(record, example)
//    }
//}
