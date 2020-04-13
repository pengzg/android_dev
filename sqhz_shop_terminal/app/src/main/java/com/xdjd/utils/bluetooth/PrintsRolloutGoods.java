package com.xdjd.utils.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;

import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.CashReportBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderDetailBean;
import com.xdjd.distribution.bean.OrderSettlementBean;
import com.xdjd.distribution.bean.OutboundDetailBean;
import com.xdjd.distribution.bean.PlaceAnOrderDetailBean;
import com.xdjd.distribution.bean.PrintGlCashBean;
import com.xdjd.distribution.bean.PrintOrderBean;
import com.xdjd.distribution.bean.ReceiptPaymentDetailBean;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/28
 *     desc   : 打印工具
 *     version: 1.0
 * </pre>
 */

public class PrintsRolloutGoods {

    /**
     * 打印分割线
     *
     * @param bluetoothSocket
     */
    public static void printsDivider(BluetoothSocket bluetoothSocket) {
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            pUtil.printDashLine();
            pUtil.printLine();
        } catch (Exception e) {
        }
    }

    /**
     * 打印订单申报单子
     *
     * @param bluetoothSocket
     * @param type            打印订单类型 1.申报;2.车销;
     * @param title           订单类型
     * @param time            时间
     * @param salesmanName    员工姓名
     * @param salesmanPhone   员工电话
     * @param t               数据列表
     * @param orderAmount     总价格
     * @param isPrintCode     是否打印商品条码,true:打印,false:不打印
     * @return
     */
    public static <T> boolean printOrderList(BluetoothSocket bluetoothSocket, int type, ClientBean clientBean, String title, String time, String orderCode,
                                         String salesmanName, String salesmanPhone, T t, String orderAmount, boolean isPrintCode) {
        return printOrderList(bluetoothSocket, type, clientBean, title, time, orderCode, salesmanName, salesmanPhone,
                t, orderAmount, null, null, null, null, null, null, isPrintCode);
    }

    /**
     * 打印订单申报单子
     *
     * @param bluetoothSocket
     * @param type       1.申报;2.车销;
     * @param title           订单类型
     * @param time            时间
     * @param salesmanName    员工姓名
     * @param salesmanPhone   员工电话
     * @param t               数据列表
     * @param orderAmount     总价格
     * @param isPrintCode     是否打印商品条码,true:打印,false:不打印
     * @return
     */
    public static <T> boolean printOrderList(BluetoothSocket bluetoothSocket, int type, ClientBean clientBean, String title, String time, String orderCode,
                                             String salesmanName, String salesmanPhone, T t, String orderAmount,
                                             String skAmount, String yhAmount, String xjAmount, String yeAmount, String ysAmount, String sfAmount,
                                             boolean isPrintCode) {
        //是否打印条码,true:打印
        boolean isPrintGoodsCode = false;

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            pUtil.printLargeText(title,1);
            pUtil.printLine();
            pUtil.printAlignment(0);

            if (!UserInfoUtils.getTicketmsgHead(UIUtils.getContext()).equals("")) {
                pUtil.printText(UserInfoUtils.getTicketmsgHead(UIUtils.getContext()));
                pUtil.printLine();
            }

            pUtil.printTwoFormatOne("客户名称:", clientBean.getCc_name());
            pUtil.printTwoFormatOne("联系人:", clientBean.getCc_contacts_name());
            pUtil.printTwoFormatOne("联系电话:", clientBean.getCc_contacts_mobile());
            pUtil.printTwoFormatOne("客户地址:", clientBean.getCc_address());

            pUtil.printText("单号:" + orderCode);
            pUtil.printLine();

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTextBold();
            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                String leftOne = isPrintGoodsCode ? "商品" : "商品/商品码";
                pUtil.printFourData(leftOne, "数量", "单价", "金额");
            } else {//58mm打印格式
                pUtil.printThreeData("数量", "单价", "金额");
            }

            pUtil.printTextBoldCancel();

            OrderSettlementBean beanSettlement = (OrderSettlementBean) t;

            for (OrderSettlementBean beanOrder : beanSettlement.getListBuildData()) {
                pUtil.printTextBold();
                switch (beanOrder.getOm_ordertype()) {
                    case BaseConfig.OrderType1:
                        pUtil.printDashText(type == 2 ? "销售" : "订单");
                        break;
                    case BaseConfig.OrderType2:
                        pUtil.printDashText("处理");
                        break;
                    case BaseConfig.OrderType3:
                        pUtil.printDashText("退货");
                        break;
                    case BaseConfig.OrderType4:
                        pUtil.printDashText("换货");
                        break;
                    case BaseConfig.OrderType5:
                        pUtil.printDashText("换货");
                        break;
                }
                pUtil.printTextBoldCancel();

                String goodsNum;//商品数量字段
                String goodsPrice;
                String goodsName;

                for (OrderSettlementBean bean : beanOrder.getListData()) {
                    if ("1".equals(bean.getOd_unit_num())) {//大小单位换算比==1,不显示大单位
                        goodsNum = bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                        goodsPrice = bean.getOd_price_min();
                    } else {
                        goodsNum = bean.getOd_goods_num_max() + bean.getOd_goods_unitname_max() + bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                        goodsPrice = bean.getOd_price_max() + "/" + bean.getOd_price_min();
                    }

                    if (BaseConfig.OrderType3 == beanOrder.getOm_ordertype() || BaseConfig.OrderType4 == beanOrder.getOm_ordertype()){
                        goodsName = "["+bean.getOd_goods_state_nameref() + "]" + bean.getOd_goods_name();
                    }else{
                        goodsName = bean.getOd_goods_name();
                    }

                    printGoodsData(pUtil, goodsName, "", bean.getOd_real_total(), goodsNum, goodsPrice,
                            bean.getGg_international_code_min(), isPrintCode);
                }
            }

            if (type == 2){//车销
                pUtil.printTextBold();
                pUtil.printDashText("结算信息");
                pUtil.printTextBoldCancel();
            }else{
                pUtil.printDashLine();
                pUtil.printLine();
            }

            pUtil.printTwoColumn("总金额:", orderAmount);

            if (type == 2) {//车销
                pUtil.printDashLine();
                pUtil.printLine();

                pUtil.printTwoColumn("优惠金额:", yhAmount);
                pUtil.printTwoColumn("现金金额:", xjAmount);
                pUtil.printTwoColumn("余额支付:", yeAmount);
//                pUtil.printTwoColumn("欠款:", ysAmount);
                pUtil.printTwoColumn("刷卡金额:", skAmount);

                pUtil.printLargeText("欠款:"+ysAmount,0);
                pUtil.printLine();
                pUtil.printLargeText("实付金额:"+sfAmount,0);
                pUtil.printLine();
            }

            pUtil.printDashLine();
            pUtil.printLine();


            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                pUtil.printTwoColumnWeight("打印时间:", time);
            } else {
                pUtil.printText("打印时间:" + time);
                pUtil.printLine();
            }
            pUtil.printTwoColumnWeight("业务员:" + salesmanName, "电话:" + salesmanPhone);
            pUtil.printTwoColumnWeight("日期:", "客户签字:");
            pUtil.printLine(1);

            if (!UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()).equals("")) {
                pUtil.printText(UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()));
                pUtil.printLine();
            }

            pUtil.printLine(4);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 订货详情打印
     *
     * @param bluetoothSocket
     * @param title            订单名称
     * @param salesmanName     员工姓名
     * @param salesmanPhone    员工电话
     * @param t                数据列表
     * @param orderAmount      总价格
     * @param noDeliveryAmount 未发货金额
     * @return
     */
    public static <T> boolean printPlaceAnOrderList(BluetoothSocket bluetoothSocket, ClientBean clientBean, String title, String orderCode,
                                                    String salesmanName, String salesmanPhone, T t, String orderAmount, String noDeliveryAmount) {

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            pUtil.printLargeText(title, 1);
            pUtil.printLine();
            pUtil.printAlignment(0);

            if (!UserInfoUtils.getTicketmsgHead(UIUtils.getContext()).equals("")) {
                pUtil.printText(UserInfoUtils.getTicketmsgHead(UIUtils.getContext()));
                pUtil.printLine();
            }

            pUtil.printTwoFormatOne("客户名称:", clientBean.getCc_name());
            //            pUtil.printTwoFormatOne("联系人:", clientBean.getCc_contacts_name());
            pUtil.printTwoFormatOne("联系电话:", clientBean.getCc_contacts_mobile());
            pUtil.printTwoFormatOne("客户地址:", clientBean.getCc_address());

            pUtil.printText("单号:" + orderCode);
            pUtil.printLine();

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTextBold();
            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                pUtil.printFourData("商品", "总数量", "未发货", "总金额");
            } else {//58mm打印格式
                pUtil.printThreeData("总数量", "未发货", "总金额");
            }
            pUtil.printTextBoldCancel();

            List<PlaceAnOrderDetailBean> list = (List<PlaceAnOrderDetailBean>) t;

            for (PlaceAnOrderDetailBean bean : list) {

                String goodsNum;//商品数量字段
                String orderSurplusNum;

                if ("1".equals(bean.getOad_unit_num())) {//大小单位换算比==1,不显示大单位
                    goodsNum = bean.getOad_goods_num() + bean.getOad_goods_unitname_min();
                    orderSurplusNum = bean.getOrder_surplus_num() + bean.getOad_goods_unitname_min();
                } else {
                    goodsNum = UnitCalculateUtils.unitStr(bean.getOad_unit(),bean.getOad_goods_num(),
                            bean.getOad_goods_unitname_max(),bean.getOad_goods_unitname_min());
                    orderSurplusNum = UnitCalculateUtils.unitStr(bean.getOad_unit(), String.valueOf(bean.getOrder_surplus_num()),
                            bean.getOad_goods_unitname_max(),bean.getOad_goods_unitname_min());
                }

                String goodsName = bean.getOad_goods_name();//商品名字+规格

                if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                    pUtil.printFourData(goodsName, goodsNum, orderSurplusNum, bean.getOad_total_amount());
                } else {//58mm打印格式
                    pUtil.printText(goodsName);
                    pUtil.printLine();
                    pUtil.printThreeData(goodsNum, orderSurplusNum, bean.getOad_total_amount());
                }
            }


            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("总金额:", orderAmount);
            pUtil.printTwoColumn("未发货金额:", noDeliveryAmount);

            pUtil.printDashLine();
            pUtil.printLine();


            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                pUtil.printTwoColumnWeight("打印时间:", StringUtils.getDate());
            } else {
                pUtil.printText("打印时间:" + StringUtils.getDate());
                pUtil.printLine();
            }
            pUtil.printTwoColumnWeight("业务员:" + salesmanName, "电话:" + salesmanPhone);
            //            pUtil.printTwoColumnWeight("日期:", "客户签字:");
            pUtil.printLine(1);

            if (!UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()).equals("")) {
                pUtil.printText(UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()));
                pUtil.printLine();
            }

            pUtil.printLine(4);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 打印商品信息
     *
     * @param pUtil
     * @param name        spmc
     * @param model       规格
     * @param totalPrice
     * @param goodsCode
     * @param isPriceCode
     * @throws IOException
     */
    private static void printGoodsData(PrintUtil pUtil, String name, String model, String totalPrice,
                                       String goodsNum, String goodsPrice, String goodsCode, boolean isPriceCode) throws IOException {

        String goodsName = name + model;//商品名字+规格
        if (!isPriceCode) {//如果不打印条形码
            if (goodsCode != null && !goodsCode.equals("")) {
                goodsName = goodsName + " / " + goodsCode;
            }
        }

        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
            pUtil.printFourData(goodsName, goodsNum,
                    goodsPrice, totalPrice);
        } else {//58mm打印格式
            pUtil.printText(goodsName);
            pUtil.printLine();
            pUtil.printThreeData(goodsNum, goodsPrice, totalPrice);
        }

        if (isPriceCode && goodsCode != null && goodsCode.length() > 0) {//如果打印商品条码
            pUtil.printText(PrintUtil.getBarcodeCmd(goodsCode));
            pUtil.printAlignment(0);
            pUtil.printFlush();
        }
    }

}
