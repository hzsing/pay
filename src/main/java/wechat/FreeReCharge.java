package wechat;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FreeReCharge {
	
	public String appid = "appid";
	public String mchid = "mchid";
	public String pay_key = "paykey";
	
	public String getSandboxnew() throws NoSuchAlgorithmException{
        String result = null;
        String nonceStr = WXPayWxUtil.getNonceStr();
        Map<String, String> signMap =new LinkedHashMap<String, String>();
        try{
            signMap.put("mch_id", mchid);
            signMap.put("nonce_str", nonceStr);
            
            String xml = WXPayWxUtil.generateSignedXml(signMap, pay_key);
            result = WXPayWxUtil.httpsRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", "POST", xml);
            
            Map<String, String> ret = WXPayWxUtil.xmlToMap(result);
            if(ret.get("return_code").equals("SUCCESS")) {
            	result = ret.get("sandbox_signkey");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
	
	public String unifiedorder(String totalFee, String sandParntkey) throws Exception {
		String out_trade_no = WXPayWxUtil.getNonceStr();
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		dataMap.put("appid", appid);
		dataMap.put("body", "订单支付");
		dataMap.put("mch_id", mchid);
		dataMap.put("nonce_str", WXPayWxUtil.getNonceStr());
		dataMap.put("notify_url", "mds.hztuen.com/api/payment/wechatNotify/async/" + out_trade_no);
		dataMap.put("openid", "oDwkf0aKRPWspmcBdCimyedCrqqU");
		dataMap.put("out_trade_no", out_trade_no);
		dataMap.put("spbill_create_ip", "150.138.207.16");
		dataMap.put("total_fee", totalFee);
		dataMap.put("trade_type", "JSAPI");

		String xml = WXPayWxUtil.generateSignedXml(dataMap, sandParntkey);
		WXPayWxUtil.httpsRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder", "POST", xml);
		return out_trade_no;
	} 
	
	public String orderQuery(String out_trade_no, String sandParntkey) throws Exception {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		dataMap.put("appid", appid);
		dataMap.put("mch_id", mchid);
		dataMap.put("out_trade_no", out_trade_no);
		dataMap.put("nonce_str", WXPayWxUtil.getNonceStr());
		
		String xml = WXPayWxUtil.generateSignedXml(dataMap, sandParntkey);
		String result = WXPayWxUtil.httpsRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery", "POST", xml);
		return result;
	}
	
	public String refund(String out_trade_no, String sandParntkey) throws Exception{
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		dataMap.put("appid", appid);
		dataMap.put("mch_id", mchid);
		dataMap.put("nonce_str", WXPayWxUtil.getNonceStr());
		dataMap.put("out_trade_no", out_trade_no);
		dataMap.put("out_refund_no", WXPayWxUtil.getNonceStr());
		dataMap.put("total_fee", "552");
		dataMap.put("refund_fee", "552");
		
		String xml = WXPayWxUtil.generateSignedXml(dataMap, sandParntkey);
		String result = WXPayWxUtil.httpsRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/refund", "POST", xml);
		return result;
	}
	
	public String refundquery(String out_trade_no, String sandParntkey) throws Exception{
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		dataMap.put("appid", appid);
		dataMap.put("mch_id", mchid);
		dataMap.put("nonce_str", WXPayWxUtil.getNonceStr());
		dataMap.put("out_trade_no", out_trade_no);
		
		String xml = WXPayWxUtil.generateSignedXml(dataMap, sandParntkey);
		String result = WXPayWxUtil.httpsRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery", "POST", xml);
		return result;
	}
	
	public String downloadbill(String sandParntkey) throws Exception {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		dataMap.put("appid", appid);
		dataMap.put("mch_id", mchid);
		dataMap.put("nonce_str", WXPayWxUtil.getNonceStr());
		dataMap.put("bill_type", "ALL");
		dataMap.put("bill_date", "20180911");
		
		String xml = WXPayWxUtil.generateSignedXml(dataMap, sandParntkey);
		String result = WXPayWxUtil.httpsRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill", "POST", xml);
		return result;
	}

	public static void main(String[] args) throws Exception {
		FreeReCharge fr = new FreeReCharge();
		String sandParntkey = fr.getSandboxnew();
		
		//1003
		String out_trade_no = fr.unifiedorder("551", sandParntkey);
		fr.orderQuery(out_trade_no, sandParntkey);

		//1004
		String out_trade_no1 = fr.unifiedorder("552", sandParntkey);
		fr.orderQuery(out_trade_no1, sandParntkey);
		fr.refund(out_trade_no1, sandParntkey);
		fr.refundquery(out_trade_no1, sandParntkey);
		
		//1005
		fr.downloadbill(sandParntkey);
	}
	
}
