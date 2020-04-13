package com.xdjd.utils.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.CashReportBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderDetailBean;
import com.xdjd.distribution.bean.OrderSettlementBean;
import com.xdjd.distribution.bean.OutboundDetailBean;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.distribution.bean.PHTaskBean;
import com.xdjd.distribution.bean.PlaceAnOrderDetailBean;
import com.xdjd.distribution.bean.PrintGlCashBean;
import com.xdjd.distribution.bean.PrintOrderBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.ReceiptPaymentDetailBean;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.utils.LogUtils;
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

public class PrintsCopy {

    /**
     * 打印测试
     *
     * @param bluetoothSocket
     */
    public static <T> boolean printReceipt(BluetoothSocket bluetoothSocket) {
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            pUtil.printDashLine();
            pUtil.printLine(5);
            // 店铺名 居中 放大
            pUtil.printText("--恭喜您,盒子分销平台打印测试成功,祝您工作愉快--");
            pUtil.printLine(5);
            pUtil.printDashLine();
            pUtil.printLine(4);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

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
     * 收付款中收欠款
     *
     * @param bluetoothSocket
     */
    public static <T> boolean printReceiptYsk(BluetoothSocket bluetoothSocket, ClientBean clientBean, String salesmanName,
                                              String salesmanPhone, String time,
                                              T t, String yhAmount, String skAmount, String xjAmount, String hjAmount) {
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");


            headPrint(pUtil,"收欠款-收欠款");
            clientPrint(pUtil,clientBean);

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTextBold();
            pUtil.printFourData("单号", "总欠款", "本次收款", "仍欠款");
            pUtil.printTextBoldCancel();

            //收付款查询中的数据
            for (ReceivableListBean bean : (List<ReceivableListBean>) t) {
                if (bean.getIsSelect() == 0) {
                    BigDecimal wsAmount;//本次收欠款
                    BigDecimal wsqkAmount;//未收欠款金额
                    if (bean.getEt_ws_amount() == null || bean.getEt_ws_amount().length() == 0) {
                        wsAmount = BigDecimal.ZERO;
                    } else {
                        wsAmount = new BigDecimal(bean.getEt_ws_amount());
                    }
                    if (bean.getWs_amount() == null || bean.getWs_amount().length() == 0) {
                        wsqkAmount = BigDecimal.ZERO;
                    } else {
                        wsqkAmount = new BigDecimal(bean.getWs_amount());
                    }

                    pUtil.printFourData(bean.getGr_sourcecode(), bean.getGr_total_amount(),
                            wsAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                            wsqkAmount.subtract(wsAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    continue;
                }
            }

            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("优惠:", yhAmount);
            pUtil.printTwoColumn("刷卡:", skAmount);
            pUtil.printTwoColumn("收款:", xjAmount);

            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("收款合计:", hjAmount);

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
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 收付款打印
     *
     * @param bluetoothSocket
     * @param type            1.收款中;2.收付款查询中;3.是单据补打中;
     */
    public static <T> boolean printReceipt(BluetoothSocket bluetoothSocket, int type, String title, String billCode, ClientBean clientBean, String salesmanName,
                                           String salesmanPhone, String time,
                                           T t, String skAmount, String yhAmount, String xjAmount, String hjAmount) {
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            String titleStr;
            if (type == 1) {
                titleStr = "收款单-预收款";
            } else if (type == 3) {//单据补打
                titleStr = "收款单-" + title;
            } else {
                titleStr = "收款单";
            }

            headPrint(pUtil, titleStr);
            if (type != 2 && billCode != null && !"".equals(billCode)) {
                pUtil.printTwoFormatOne("收款单号", billCode);
            }
            clientPrint(pUtil, clientBean);

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            if (type == 2) {
                pUtil.printTextBold();
                pUtil.printFourData("单号 收款类型", "现金", "刷卡", "优惠");
                pUtil.printTextBoldCancel();

                //收付款查询中的数据
                for (ReceiptPaymentDetailBean bean : (List<ReceiptPaymentDetailBean>) t) {
                    if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                        pUtil.printText(bean.getBillCode());
                        pUtil.printLine();
                        pUtil.printFourData("     " + bean.getTypeName(), bean.getCashAmount(), bean.getCardAmount(), bean.getDiscountAmount());
                    } else {
                        pUtil.printFourData(bean.getBillCode() + " " + bean.getTypeName(),
                                bean.getCashAmount(), bean.getCardAmount(), bean.getDiscountAmount());
                    }
                }

                pUtil.printDashLine();
                pUtil.printLine();
            }

            if (type == 1) {
                pUtil.printTwoColumn("刷卡:", skAmount);
                pUtil.printTwoColumn("收款:", xjAmount);
                pUtil.printDashLine();
                pUtil.printLine();
                pUtil.printTwoColumn("合计:", hjAmount);
                pUtil.printDashLine();
                pUtil.printLine();
            }

            if (type == 3) {
                PrintGlCashBean bean = (PrintGlCashBean) t;
                pUtil.printTwoColumn("优惠金额:", bean.getYhAmount());
                pUtil.printTwoColumn("现金金额:", bean.getXjAmount());
                pUtil.printTwoColumn("余额支付:", bean.getYsAmount());
                pUtil.printTwoColumn("欠款:", bean.getYskAmount());
                pUtil.printTwoColumn("刷卡金额:", bean.getSkAmount());

                pUtil.printDashLine();
                pUtil.printLine();

                pUtil.printTwoColumn("实付金额:", bean.getSfkAmount());
                //                for (PrintGlCashBean bean : (List<PrintGlCashBean>) t) {
                //                    pUtil.printTwoColumn(bean.getBi_name(), bean.getGcd_amount());
                //                    pUtil.printLine();
                //                }
                pUtil.printDashLine();
                pUtil.printLine();
            }

            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                pUtil.printTwoColumnWeight("打印时间:", time);
            } else {
                pUtil.printText("打印时间:" + time);
                pUtil.printLine();
            }

            if (type != 4) {
                pUtil.printTwoColumnWeight("业务员:" + salesmanName, "电话:" + salesmanPhone);
            }

            pUtil.printTwoColumnWeight("日期:", "客户签字:");
            pUtil.printLine(1);

            if (!UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()).equals("")) {
                pUtil.printText(UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()));
                pUtil.printLine();
            }

            pUtil.printLine(4);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 现金日报打印
     *
     * @param bluetoothSocket
     */
    public static boolean printCash(BluetoothSocket bluetoothSocket, CashReportBean bean, String salesmanName,
                                    String salesmanPhone) {
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            pUtil.printLargeText("现金日报", 1);
            pUtil.printLine();
            pUtil.printAlignment(0);

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            if (UserInfoUtils.getCashReportType(UIUtils.getContext()).equals("2")) {//精简
                pUtil.printTextBold();
                pUtil.printThreeData("客户", "上交款", "总金额");
                pUtil.printTextBoldCancel();
            } else if (UserInfoUtils.getCashReportType(UIUtils.getContext()).equals("3")) {//详细
                pUtil.printTextBold();
                pUtil.printThreeData("客户", "总金额", "收款明细");
                pUtil.printTextBoldCancel();
            }

            if (UserInfoUtils.getCashReportType(UIUtils.getContext()).equals(Comon.CashType2)) {//精简
                for (CashReportBean b : bean.getListData()) {
                    pUtil.printThreeData(b.getCustmerName(), b.getPaidAmount(), b.getTotalAmount());
                    pUtil.printLine();
                }
            } else if (UserInfoUtils.getCashReportType(UIUtils.getContext()).equals(Comon.CashType3)) {//详细
                for (CashReportBean b : bean.getListData()) {
                    pUtil.printThreeData(b.getCustmerName(), b.getTotalAmount(), "上缴款:" + b.getPaidAmount() + "  刷卡:" + b.getCardAmount() + "  应收金额:" + b.getRespondReceivableAmount() +
                            "  使用金额:" + b.getUseAmount() + "  优惠金额:" + b.getDiscountAmount() + "  刷卡费用:" + b.getCardFee());
                }
            }

            if (!UserInfoUtils.getCashReportType(UIUtils.getContext()).equals("1")) {
                pUtil.printDashLine();
                pUtil.printLine();
            }
            pUtil.printTextEnter("上缴金额:" + (bean.getPaidAmount().equals("0") ? "0.00" : bean.getPaidAmount()));
            pUtil.printTextEnter("应收金额:" + (bean.getRespondReceivableAmount().equals("0") ? "0.00" : bean.getRespondReceivableAmount()));
            pUtil.printTextEnter("刷卡金额:" + (bean.getCardAmount().equals("0") ? "0.00" : bean.getCardAmount()));
            pUtil.printTextEnter("刷卡费用:" + (bean.getCardFee().equals("0") ? "0.00" : bean.getCardFee()));
            pUtil.printTextEnter("销售金额:" + (bean.getSaleAmount().equals("0") ? "0.00" : bean.getSaleAmount()));
            pUtil.printTextEnter("订货金额:" + (bean.getGoodsAmount().equals("0") ? "0.00" : bean.getGoodsAmount()));
            pUtil.printTextEnter("使用金额:" + (bean.getUseAmount().equals("0") ? "0.00" : bean.getUseAmount()));
            pUtil.printTextEnter("优惠金额:" + (bean.getDiscountAmount().equals("0") ? "0.00" : bean.getDiscountAmount()));
            pUtil.printTextEnter("收款金额:" + (bean.getReceivableAmount().equals("0") ? "0.00" : bean.getReceivableAmount()));
            pUtil.printTextEnter("付款费用:" + (bean.getPayAmount().equals("0") ? "0.00" : bean.getPayAmount()));
            pUtil.printTextEnter("退款转预收:" + (bean.getRefundGiroAmount().equals("0") ? "0.00" : bean.getRefundGiroAmount()));
            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                pUtil.printTwoColumnWeight("打印时间:", StringUtils.getDate());
            } else {
                pUtil.printText("打印时间:" + StringUtils.getDate());
                pUtil.printLine();
            }
            pUtil.printTwoColumnWeight("业务员:" + salesmanName, "电话:" + salesmanPhone);
            pUtil.printLine(1);

            pUtil.printLine(4);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 打印订单申报单子 type--打印订单类型 1.申报;2.车销;3.配送任务;4.出库查询;5要货申请;6退货申请;7.订单查询;8订货;9.去铺货;10.铺货销售;11.铺货撤货;
     *                        12.铺货单;13.销售单;14.撤货单;15.返陈列;16.铺货申报;17.铺货配送任务打印;18.铺货申报补打
     * @param bluetoothSocket
     * @param
     * @return
     */
    public static <T> boolean printOrder(BluetoothSocket bluetoothSocket, PrintParamBean paramBean) {
        //是否打印条码,true:打印
        boolean isPrintGoodsCode = false;

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            //头部信息
            headPrint(pUtil, paramBean.getTitle());
            //客户信息
            if (paramBean.getType() != 5 && paramBean.getType() != 6 && paramBean.getClientBean() != null) {
                clientPrint(pUtil, paramBean.getClientBean());
            }

            if (paramBean.getOrderCode() != null && !"".equals(paramBean.getOrderCode())) {//单号不为空时才打印
                pUtil.printText("单号:" + paramBean.getOrderCode());
                pUtil.printLine();
            }

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            //打印商品列表表头标题
            pUtil.printTextBold();
            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                String leftOne = isPrintGoodsCode ? "商品" : "商品/商品码";
                pUtil.printFourData(leftOne, "数量", "单价", "金额");
            } else {//58mm打印格式
                pUtil.printThreeData("数量", "单价", "金额");
            }

            pUtil.printTextBoldCancel();

            String goodsNum;//商品数量字段
            String goodsPrice;
            //打印商品列表
            switch (paramBean.getType()) {
                case 1:
                case 2:
                    for (OrderSettlementBean bean : (List<OrderSettlementBean>) paramBean.getT()) {
                        if ("1".equals(bean.getOd_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsNum = bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                            goodsPrice = bean.getOd_price_min();
                        } else {
                            goodsNum = bean.getOd_goods_num_max() + bean.getOd_goods_unitname_max() + bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                            goodsPrice = bean.getOd_price_max() + "/" + bean.getOd_price_min();
                        }

                        printGoodsData(pUtil, bean.getOd_goods_name(), "", bean.getOd_real_total(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 5:
                case 6:
                case 15:
                    for (GoodsBean bean : (List<GoodsBean>) paramBean.getT()) {
                        if ("1".equals(bean.getGgp_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsNum = bean.getMinNum() + bean.getGg_unit_min_nameref();
                            goodsPrice = bean.getMin_price();
                        } else {
                            goodsNum = bean.getMaxNum() + bean.getGg_unit_max_nameref() + bean.getMinNum() + bean.getGg_unit_min_nameref();
                            goodsPrice = bean.getMaxPrice() + "/" + bean.getMinPrice();
                        }

                        printGoodsData(pUtil, bean.getGg_title(), bean.getGg_model(), bean.getTotalPrice(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 3:
                    for (OrderSettlementBean bean : (List<OrderSettlementBean>) paramBean.getT()) {
                        if ("1".equals(bean.getOd_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsNum = bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                            goodsPrice = bean.getOd_price_min();
                        } else {
                            goodsNum = bean.getOd_goods_num_max() + bean.getOd_goods_unitname_max() +
                                    bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                            goodsPrice = bean.getOd_price_max() + "/" + bean.getOd_price_min();
                        }

                        String goodsName;
                        if (bean.getOd_goods_state_nameref() == null || bean.getOd_goods_state_nameref().length() == 0) {
                            goodsName = bean.getOd_goods_name();
                        } else {
                            goodsName = "[" + bean.getOd_goods_state_nameref() + "]" + bean.getOd_goods_name();
                        }

                        printGoodsData(pUtil, goodsName, "", bean.getOd_real_total(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 4:
                    for (OutboundDetailBean bean : (List<OutboundDetailBean>) paramBean.getT()) {
                        if ("1".equals(bean.getEii_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsNum = bean.getEii_goods_quantity_min() + bean.getEii_unit_min();
                            goodsPrice = bean.getEii_goods_price_min();
                        } else {
                            goodsNum = bean.getEii_goods_quantity_max() + bean.getEii_unit_max() +
                                    bean.getEii_goods_quantity_min() + bean.getEii_unit_min();
                            goodsPrice = bean.getEii_goods_price_max() + "/" + bean.getEii_goods_price_min();
                        }

                        String goodsName;
                        if (bean.getEii_goods_state_nameref() == null || bean.getEii_goods_state_nameref().length() == 0) {
                            goodsName = bean.getEii_goods_name();
                        } else {
                            goodsName = "[" + bean.getEii_goods_state_nameref() + "]" + bean.getEii_goods_name();
                        }
                        printGoodsData(pUtil, goodsName, "", bean.getEii_goods_amount(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 7:
                    for (OrderDetailBean bean : (List<OrderDetailBean>) paramBean.getT()) {
                        if ("1".equals(bean.getOd_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsNum = bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                            goodsPrice = bean.getOd_price_min();
                        } else {
                            goodsNum = bean.getOd_goods_num_max() + bean.getOd_goods_unitname_max() +
                                    bean.getOd_goods_num_min() + bean.getOd_goods_unitname_min();
                            goodsPrice = bean.getOd_price_max() + "/" + bean.getOd_price_min();
                        }

                        String goodsName;
                        if (bean.getOd_goods_state_nameref() == null || bean.getOd_goods_state_nameref().length() == 0) {
                            goodsName = bean.getOd_goods_name();
                        } else {
                            goodsName = "[" + bean.getOd_goods_state_nameref() + "]" + bean.getOd_goods_name();
                        }

                        printGoodsData(pUtil, goodsName, "", bean.getOd_real_total(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 8://订货
                    for (GoodsBean bean : (List<GoodsBean>) paramBean.getT()) {
                        if ("1".equals(bean.getGgp_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsNum = bean.getMinNum() + bean.getGg_unit_min_nameref();
                            goodsPrice = bean.getMinPrice();
                        } else {
                            goodsNum = bean.getMaxNum() + bean.getGg_unit_max_nameref() +
                                    bean.getMinNum() + bean.getGg_unit_min_nameref();
                            goodsPrice = bean.getMaxPrice() + "/" + bean.getMinPrice();
                        }
                        String goodsName;
                        goodsName = bean.getGg_title();

                        printGoodsData(pUtil, goodsName, "", bean.getTotalPrice(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 9://铺货
                case 16://铺货申报
                    for (OrderSettlementBean bean : (List<OrderSettlementBean>) paramBean.getT()) {
                        if ("1".equals(bean.getOd_unit_num())) {//大小单位换算比==1,不显示大单位
                            goodsPrice = bean.getOd_price_min();
                        } else {
                            goodsPrice = bean.getOd_price_max() + "/" + bean.getOd_price_min();
                        }
                        goodsNum = UnitCalculateUtils.goodsNumDesc(bean.getOd_unit_num(), bean.getOd_goods_num_max(), bean.getOd_goods_num_min(),
                                bean.getOd_goods_unitname_max(), bean.getOd_goods_unitname_min());

                        String goodsName;
                        goodsName = bean.getOd_goods_name();

                        printGoodsData(pUtil, goodsName, "", bean.getOd_real_total(), goodsNum, goodsPrice,
                                bean.getGg_international_code_min(), paramBean.isPrintCode());
                    }
                    break;
                case 10://铺货销售
                case 11://铺货撤货
                    for (PHOrderDetailBean bean0 : (List<PHOrderDetailBean>) paramBean.getT()) {
                        pUtil.printDashText(bean0.getOrderCode());
                        for (PHOrderDetailBean bean : bean0.getListGoodsData()) {
                            if ("1".equals(bean.getUnitNum())) {//大小单位换算比==1,不显示大单位
                                //                                goodsNum = bean.getMinNum() + bean.getMinName();
                                goodsPrice = bean.getMinPrice();
                            } else {
                                //                                goodsNum = bean.getMaxNum() + bean.getMaxName() +
                                //                                        bean.getMinNum() + bean.getMinName();
                                goodsPrice = bean.getMaxPrice() + "/" + bean.getMinPrice();
                            }

                            goodsNum = UnitCalculateUtils.goodsNumDesc(bean.getUnitNum(), bean.getMaxNum(), bean.getMinNum(),
                                    bean.getMaxName(), bean.getMinName());
                            String goodsName;
                            goodsName = bean.getGoodsName();
                            printGoodsData(pUtil, goodsName, "", bean.getTotalPrice(), goodsNum, goodsPrice,
                                    bean.getGoodsCode(), paramBean.isPrintCode());
                        }
                    }
                    break;
                case 12://铺货单
                case 18:
                    for (PHOrderDetailBean bean : (List<PHOrderDetailBean>) paramBean.getT()) {
                        String priceStr;
                        if ("1".equals(bean.getUnitNum())) {
                            priceStr = bean.getMinPrice();
                        } else {
                            priceStr = bean.getMaxPrice() + "/" + bean.getMinPrice();
                        }
                        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                            pUtil.printFourData(bean.getGoodsName(), bean.getPhTotalNum_desc(),
                                    priceStr, bean.getTotalAmount());
                        } else {//58mm打印格式
                            pUtil.printText(bean.getGoodsName());
                            pUtil.printLine();
                            pUtil.printThreeData(bean.getPhTotalNum_desc(),
                                    priceStr, bean.getTotalAmount());
                        }
                    }
                    break;
                case 13://销售
                    for (PHOrderDetailBean bean0 : (List<PHOrderDetailBean>) paramBean.getT()) {
                        pUtil.printDashText(bean0.getApply_order_code());
                        for (PHOrderDetailBean bean : bean0.getListData()) {
                            if ("1".equals(bean.getUnitNum())) {//大小单位换算比==1,不显示大单位
                                goodsPrice = bean.getMinPrice();
                            } else {
                                goodsPrice = bean.getMaxPrice() + "/" + bean.getMinPrice();
                            }
                            String goodsName;
                            goodsName = bean.getGoodsName();
                            printGoodsData(pUtil, goodsName, "", bean.getTotalAmount(), bean.getPhSaleNum_desc(), goodsPrice,
                                    bean.getGoodsCode(), paramBean.isPrintCode());
                        }
                    }
                    break;
                case 14:
                    for (PHOrderDetailBean bean0 : (List<PHOrderDetailBean>) paramBean.getT()) {
                        pUtil.printDashText(bean0.getApply_order_code());
                        for (PHOrderDetailBean bean : bean0.getListData()) {
                            if ("1".equals(bean.getUnitNum())) {//大小单位换算比==1,不显示大单位
                                goodsPrice = bean.getMinPrice();
                            } else {
                                goodsPrice = bean.getMaxPrice() + "/" + bean.getMinPrice();
                            }
                            String goodsName;
                            goodsName = bean.getGoodsName();
                            printGoodsData(pUtil, goodsName, "", bean.getTotalAmount(), bean.getRefundNum_desc(), goodsPrice,
                                    bean.getGoodsCode(), paramBean.isPrintCode());
                        }
                    }
                    break;
                case 17://铺货配送任务
                    for (PHTaskBean bean : (List<PHTaskBean>) paramBean.getT()) {
                        String priceStr;
                        if ("1".equals(bean.getOad_unit_num())) {
                            priceStr = bean.getOad_price_min();
                        } else {
                            priceStr = bean.getOad_price_max() + "/" + bean.getOad_price_min();
                        }
                        goodsNum = UnitCalculateUtils.goodsNumDesc(bean.getOad_unit_num(), bean.getOad_goods_num_max(), bean.getOad_goods_num_min(),
                                bean.getOad_goods_unitname_max(), bean.getOad_goods_unitname_min());
                        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                            pUtil.printFourData(bean.getOad_goods_name(), goodsNum,
                                    priceStr, bean.getOad_total_amount());
                        } else {//58mm打印格式
                            pUtil.printText(bean.getOad_goods_name());
                            pUtil.printLine();
                            pUtil.printThreeData(goodsNum,
                                    priceStr, bean.getOad_total_amount());
                        }
                    }
                    break;
            }
            //打印单据金额
            if (paramBean.getType() == 2 || paramBean.getType() == 3 || paramBean.getType() == 10 ||
                    paramBean.getType() == 13 || (paramBean.getType() == 4 && paramBean.getClientBean() != null &&
                    paramBean.getSfAmount() != null) || (paramBean.getType() == 7 && paramBean.getOrderStatus().equals("6"))) {
                paymentParamsPrint(pUtil,paramBean);
            } else if (paramBean.getType() == 8) {
                pUtil.printDashLine();
                pUtil.printLine();
                pUtil.printTwoColumn("优惠金额:", paramBean.getYhAmount());
                pUtil.printTwoColumn("现金金额:", paramBean.getXjAmount());
                pUtil.printTwoColumn("刷卡金额:", paramBean.getSkAmount());
                pUtil.printDashLine();
                pUtil.printLine();

                pUtil.printLargeText("总金额:" + paramBean.getTotalAmount(), 0);
                pUtil.printLine();
                pUtil.printLargeText("实付金额:" + paramBean.getSfAmount(), 0);
                pUtil.printLine();
                pUtil.printDashLine();
                pUtil.printLine();

            } else {
                paymentParamsPrint1(pUtil,paramBean);
            }

            //打印铺货历史单据
            PHOrderDetailBean beanRollout = (PHOrderDetailBean) paramBean.getT1();
            if ((paramBean.getType() == 14 || paramBean.getType() == 13 || paramBean.getType() == 11 || paramBean.getType() == 10) &&
                    beanRollout != null && beanRollout.getListData() != null && beanRollout.getListData().size() > 0) {
                printHistoryPhList(pUtil,beanRollout);
            }
            //打印票据尾部信息
            bottomPrint(pUtil, paramBean);
            return true;
        } catch (IOException e) {
            LogUtils.e("异常",e.toString());
            return false;
        }
    }

    /**
     * 打印订单申报和车销下单时的单据
     *
     * @param bluetoothSocket
     * @return
     */
    public static <T> boolean printOrderList(BluetoothSocket bluetoothSocket, PrintParamBean paramBean) {
        //type            打印订单类型 1.申报;2.车销;
        boolean isPrintGoodsCode = false;

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            headPrint(pUtil, paramBean.getTitle());
            clientPrint(pUtil, paramBean.getClientBean());

            pUtil.printText("单号:" + paramBean.getOrderCode());
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

            OrderSettlementBean beanSettlement = (OrderSettlementBean) paramBean.getT();

            for (OrderSettlementBean beanOrder : beanSettlement.getListBuildData()) {
                pUtil.printTextBold();
                switch (beanOrder.getOm_ordertype()) {
                    case BaseConfig.OrderType1:
                        pUtil.printDashText(paramBean.getType() == 2 ? "销售" : "订单");
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
                        pUtil.printDashText("还货");
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

                    if (BaseConfig.OrderType3 == beanOrder.getOm_ordertype() || BaseConfig.OrderType4 == beanOrder.getOm_ordertype()) {
                        goodsName = "[" + bean.getOd_goods_state_nameref() + "]" + bean.getOd_goods_name();
                    } else {
                        goodsName = bean.getOd_goods_name();
                    }

                    printGoodsData(pUtil, goodsName, "", bean.getOd_real_total(), goodsNum, goodsPrice,
                            bean.getGg_international_code_min(), paramBean.isPrintCode());
                }
            }

            if (paramBean.getType() == 2) {//车销
                paymentParamsPrint(pUtil, paramBean);
            } else {
                pUtil.printDashLine();
                pUtil.printLine();
                pUtil.printTwoColumn("总金额:", paramBean.getTotalAmount());
                pUtil.printDashLine();
                pUtil.printLine();
            }

            bottomPrint(pUtil, paramBean);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 订货详情打印
     *
     * @param bluetoothSocket
     * @return
     */
    public static <T> boolean printPlaceAnOrderList(BluetoothSocket bluetoothSocket, PrintParamBean paramBean) {

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            headPrint(pUtil, paramBean.getTitle());
            clientPrint(pUtil, paramBean.getClientBean());

            pUtil.printText("单号:" + paramBean.getOrderCode());
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

            List<PlaceAnOrderDetailBean> list = (List<PlaceAnOrderDetailBean>) paramBean.getT();

            for (PlaceAnOrderDetailBean bean : list) {

                String goodsNum;//商品数量字段
                String orderSurplusNum;

                if ("1".equals(bean.getOad_unit_num())) {//大小单位换算比==1,不显示大单位
                    goodsNum = bean.getOad_goods_num() + bean.getOad_goods_unitname_min();
                    orderSurplusNum = bean.getOrder_surplus_num() + bean.getOad_goods_unitname_min();
                } else {
                    goodsNum = UnitCalculateUtils.unitStr(bean.getOad_unit(), bean.getOad_goods_num(),
                            bean.getOad_goods_unitname_max(), bean.getOad_goods_unitname_min());
                    orderSurplusNum = UnitCalculateUtils.unitStr(bean.getOad_unit(), String.valueOf(bean.getOrder_surplus_num()),
                            bean.getOad_goods_unitname_max(), bean.getOad_goods_unitname_min());
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

            PlaceAnOrderDetailBean orderBean = (PlaceAnOrderDetailBean) paramBean.getT1();
            pUtil.printTwoColumn("优惠金额:", orderBean.getDiscountAmount());
            pUtil.printTwoColumn("现金金额:", orderBean.getXjAmount());
            pUtil.printTwoColumn("刷卡金额:", orderBean.getSkAmount());
            pUtil.printTwoColumn("未发货金额:", orderBean.getNo_delivery_amount());

            pUtil.printDashLine();
            pUtil.printLine();
            pUtil.printLargeText("总金额:" + orderBean.getTotal_amount(), 0);
            pUtil.printLine();

            pUtil.printLargeText("实付金额:" + orderBean.getSfkAmount(), 0);
            pUtil.printLine();

            pUtil.printDashLine();
            pUtil.printLine();


            bottomPrint(pUtil,paramBean);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 补打打印订单
     *
     * @param bluetoothSocket
     * @param paramBean       打印参数bean
     * @return
     */
    public static boolean printOrderBu(BluetoothSocket bluetoothSocket, PrintParamBean paramBean) {
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");

            headPrint(pUtil, paramBean.getTitle());
            clientPrint(pUtil, paramBean.getClientBean());

            pUtil.printText("单号:" + paramBean.getOrderCode());
            pUtil.printLine();
            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTextBold();
            if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                pUtil.printFourData("商品/商品码", "数量", "单价", "金额");
            } else {//58mm打印格式
                pUtil.printThreeData("数量", "单价", "金额");
            }
            pUtil.printTextBoldCancel();

            PrintOrderBean params = (PrintOrderBean) paramBean.getT();//打印参数bean

            String goodsNum;//商品数量字段
            String goodsPrice;
            for (PrintOrderBean bean : params.getDataList()) {
                if ((bean.getOd_delivery_num_max() == null || "0".equals(bean.getOd_delivery_num_max())) &&
                        (bean.getOd_delivery_num_min() == null || "0".equals(bean.getOd_delivery_num_min()))) {
                    continue;
                }
                if ("1".equals(bean.getOd_unit_num())) {//大小单位换算比==1,不显示大单位
                    goodsNum = bean.getOd_delivery_num_min() + bean.getOd_goods_unitname_min();
                    goodsPrice = bean.getOd_price_min();
                } else {
                    goodsNum = bean.getOd_delivery_num_max() + bean.getOd_goods_unitname_max()
                            + bean.getOd_delivery_num_min() + bean.getOd_goods_unitname_min();
                    goodsPrice = bean.getOd_price_max() + "/" + bean.getOd_price_min();
                }
                String goodsName;
                if (bean.getOd_goods_state_nameref() == null || bean.getOd_goods_state_nameref().length() == 0) {
                    goodsName = bean.getOd_goods_name();
                } else {
                    goodsName = "[" + bean.getOd_goods_state_nameref() + "]" + bean.getOd_goods_name();
                }

                printGoodsData(pUtil, goodsName, "", bean.getOd_real_total(), goodsNum, goodsPrice,
                        bean.getGg_international_code_min(), paramBean.isPrintCode());
            }

            //type 2 订单申报  3.车销
            if (paramBean.getType() == 3 || params.getStats() == 6) {
                paymentParamsPrint(pUtil, paramBean);
            } else {
                pUtil.printDashLine();
                pUtil.printLine();
                pUtil.printTwoColumn("总金额:", paramBean.getTotalAmount());
                pUtil.printDashLine();
                pUtil.printLine();
            }
            bottomPrint(pUtil, paramBean);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 打印小票头部信息和单据标题
     *
     * @param pUtil
     * @param title
     * @throws IOException
     */
    private static void headPrint(PrintUtil pUtil, String title) throws IOException {
        pUtil.printLargeText(title, 1);
        pUtil.printLine();
        pUtil.printAlignment(0);
        //小票头部信息
        if (!UserInfoUtils.getTicketmsgHead(UIUtils.getContext()).equals("")) {
            pUtil.printText(UserInfoUtils.getTicketmsgHead(UIUtils.getContext()));
            pUtil.printLine();
        }
    }

    /**
     * 客户信息打印
     *
     * @param clientBean
     */
    private static void clientPrint(PrintUtil pUtil, ClientBean clientBean) throws IOException {
        pUtil.printTwoFormatOne("客户名称:", clientBean.getCc_name());
        pUtil.printTwoFormatOne("联系人:", clientBean.getCc_contacts_name());
        pUtil.printTwoFormatOne("联系电话:", clientBean.getCc_contacts_mobile());
        pUtil.printTwoFormatOne("客户地址", clientBean.getCc_address());
    }

    /**
     * 打印收款信息--只打印总金额
     * @param pUtil
     * @param paramBean
     */
    private static void paymentParamsPrint1(PrintUtil pUtil, PrintParamBean paramBean) throws IOException {
        pUtil.printDashLine();
        pUtil.printLine();
        if (paramBean.getType() == 9 || paramBean.getType() == 12 || paramBean.getType() == 17) {//铺货下单是显示总欠款
            pUtil.printTwoColumn("欠款金额:", paramBean.getTotalAmount());
        }else{
            pUtil.printTwoColumn("总金额:", paramBean.getTotalAmount());
        }

        pUtil.printDashLine();
        pUtil.printLine();
    }

    /**
     * 打印收款信息
     *
     * @param pUtil
     * @param paramBean
     * @throws IOException
     */
    private static void paymentParamsPrint(PrintUtil pUtil, PrintParamBean paramBean) throws IOException {
        //String skAmount, String yhAmount, String xjAmount, String yeAmount, String ysAmount, String sfAmount
        pUtil.printTextBold();
        pUtil.printDashText("结算信息");
        pUtil.printTextBoldCancel();

        pUtil.printTwoColumn("优惠金额:", paramBean.getYhAmount());
        pUtil.printTwoColumn("现金金额:", paramBean.getXjAmount());
        pUtil.printTwoColumn("余额支付:", paramBean.getYeAmount());

        BigDecimal ysAmount;
        if (paramBean.getYsAmount() == null || paramBean.getYsAmount().length() == 0) {
            ysAmount = BigDecimal.ZERO;
        } else {
            ysAmount = new BigDecimal(paramBean.getYsAmount());
        }
        if (ysAmount.compareTo(BigDecimal.ZERO) == -1) {//如果是负数,显示欠款转预收
            pUtil.printTwoColumn("欠款转预收:", paramBean.getYsAmount());
        } else {
            pUtil.printTwoColumn("欠款:", paramBean.getYsAmount());
        }
        pUtil.printTwoColumn("刷卡金额:", paramBean.getSkAmount());

        pUtil.printDashLine();
        pUtil.printLine();
        if (paramBean.getType() == 9 || paramBean.getType() == 12 || paramBean.getType() == 17) {//铺货下单是显示总欠款
            pUtil.printLargeText("欠款金额:" + paramBean.getTotalAmount(), 0);
        } else {
            pUtil.printLargeText("总金额:" + paramBean.getTotalAmount(), 0);
        }
        pUtil.printLine();

        if (paramBean.getSfAmount() != null) {
            pUtil.printLargeText("实付金额:" + paramBean.getSfAmount(), 0);
            pUtil.printLine();

        }
        pUtil.printDashLine();
        pUtil.printLine();
    }

    /**
     * 打印底部小票信息(底部条码、业务员信息、底部小票提示信息)
     *
     * @param pUtil
     * @param paramBean
     * @throws IOException
     */
    private static void bottomPrint(PrintUtil pUtil, PrintParamBean paramBean) throws IOException {
        /*if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                //打印单号条形码
                pUtil.printText(PrintUtil.getBarcodeCmd(orderCode));
                //            pUtil.printBitmap(bitmap);
                pUtil.printAlignment(0);
                pUtil.printFlush();
            }else{
                Bitmap bitmap1 = null;
                try {
                    bitmap1 = ZXingUtil.CreateBrandCode(orderCode, 980, 60);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                pUtil.printBitmap(bitmap1);
                pUtil.printText(orderCode);
                pUtil.printLine();
                pUtil.printAlignment(0);
                pUtil.printFlush();
            }*/
        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
            pUtil.printTwoColumnWeight("打印时间:", paramBean.getTime());
        } else {
            pUtil.printText("打印时间:" + paramBean.getTime());
            pUtil.printLine();
        }
        pUtil.printTwoColumnWeight("业务员:" + paramBean.getUserBean().getBud_name(),
                "电话:" + paramBean.getUserBean().getMobile());
        pUtil.printTwoColumnWeight("日期:", "客户签字:");

        if (paramBean.getSignImg()!=null){//客户签字图片
            /*// 获得图片的宽高.
            int width = paramBean.getSignImg().getWidth();
            int height = paramBean.getSignImg().getHeight();
            // 计算缩放比例.
            float scaleWidth = ((float) 980) / width;
            float scaleHeight = ((float) 720) / height;
            // 取得想要缩放的matrix参数.
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片.
            Bitmap newbm = Bitmap.createBitmap(paramBean.getSignImg(), 0, 0, width, height, matrix, true);*/
            try{
                pUtil.printBitmap(paramBean.getSignImg());
            } catch (Exception e){
                LogUtils.e("printImgException",e.toString());
            }
            pUtil.printLine();
            pUtil.printAlignment(0);
            pUtil.printFlush();
        }

        if ((paramBean.getType() == 9 || paramBean.getType() == 11 || paramBean.getType() == 12 || paramBean.getType() == 14) &&
                paramBean.getRemarks() != null && paramBean.getRemarks().length() > 0) {
            pUtil.printText("备注:" + paramBean.getRemarks());
            pUtil.printLine();
        }
        pUtil.printLine(1);

        if (!UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()).equals("")) {
            pUtil.printText(UserInfoUtils.getTicketmsgBottom(UIUtils.getContext()));
            pUtil.printLine();
        }
        pUtil.printLine(4);
    }

    /**
     * 打印铺货历史单据
     * @param pUtil
     * @param beanRollout
     */
    public static void printHistoryPhList(PrintUtil pUtil, PHOrderDetailBean beanRollout) throws IOException {
        pUtil.printDashText("历史铺货单据");
        String goodsPrice;//商品价格
        for (PHOrderDetailBean bean0 : beanRollout.getListData()) {
            pUtil.printDashText(bean0.getOrderCode());
            for (PHOrderDetailBean bean : bean0.getListGoodsData()) {
                if ("1".equals(bean.getUnitNum())) {//大小单位换算比==1,不显示大单位
                    goodsPrice = bean.getMinPrice();
                } else {
                    goodsPrice = bean.getMaxPrice() + "/" + bean.getMinPrice();
                }
                String goodsName;
                goodsName = bean.getGoodsName();

                if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
                    printGoodsData2(pUtil, goodsName, "", bean.getSyGoodsAmount(), bean.getPhTotalNum_desc(), goodsPrice,
                            "", false);
                    pUtil.printThreeData("已撤货", "已结算", "未结算");
                    pUtil.printThreeData(bean.getRefundNum_desc(), bean.getPhSaleNum_desc(), bean.getResidueNum_desc());
                } else {//58mm打印格式
                    printGoodsData2(pUtil, goodsName, "", bean.getSyGoodsAmount(), bean.getPhTotalNum_desc(), goodsPrice,
                            "", false);
                    pUtil.printThreeData("已撤货", "已结算", "未结算");
                    pUtil.printThreeData(bean.getRefundNum_desc(), bean.getPhSaleNum_desc(), bean.getResidueNum_desc());
                }
            }
        }
        pUtil.printDashLine();
        pUtil.printLine();
        pUtil.printLargeText("剩余总欠款:" + beanRollout.getTotalDebt(), 0);
        pUtil.printLine();
        pUtil.printDashLine();
        pUtil.printLine();
        pUtil.printLine(2);
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

    private static void printGoodsData2(PrintUtil pUtil, String name, String model, String totalPrice,
                                        String goodsNum, String goodsPrice, String goodsCode, boolean isPriceCode) throws IOException {

        String goodsName = name + model;//商品名字+规格
        if (!isPriceCode) {//如果不打印条形码
            if (goodsCode != null && !goodsCode.equals("")) {
                goodsName = goodsName + " / " + goodsCode;
            }
        }

        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
            String leftOne = isPriceCode ? "商品" : "商品/商品码";
            pUtil.printTextBold();
            pUtil.printFourData(leftOne, "总数量", "单价", "未结金额");
            pUtil.printTextBoldCancel();

            pUtil.printFourData(goodsName, goodsNum,
                    goodsPrice, totalPrice);
        } else {//58mm打印格式
            pUtil.printText(goodsName);
            pUtil.printLine();
            pUtil.printTextBold();
            pUtil.printThreeData("总数量", "单价", "未结金额");
            pUtil.printTextBoldCancel();

            pUtil.printThreeData(goodsNum, goodsPrice, totalPrice);
        }

        if (isPriceCode && goodsCode != null && goodsCode.length() > 0) {//如果打印商品条码
            pUtil.printText(PrintUtil.getBarcodeCmd(goodsCode));
            pUtil.printAlignment(0);
            pUtil.printFlush();
        }
    }


    /*************************************************************************
     * 我们的热敏打印机是RP-POS80S或RP-POS80P或RP-POS80CS或RP-POS80CP打印机
     * 360*360的图片，8个字节（8个像素点）是一个二进制，将二进制转化为十进制数值
     * y轴：24个像素点为一组，即360就是15组（0-14）
     * x轴：360个像素点（0-359）
     * 里面的每一组（24*360），每8个像素点为一个二进制，（每组有3个，3*8=24）
     **************************************************************************/
    /**
     * 把一张Bitmap图片转化为打印机可以打印的bit(将图片压缩为360*360)
     * 效率很高（相对于下面）
     *
     * @param bit
     * @return
     */
    public static byte[] draw2PxPoint(Bitmap bit) {
        byte[] data = new byte[16290];
        int k = 0;
        for (int j = 0; j < 10; j++) {
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI。
            data[k++] = 0x68;
            data[k++] = 0x01;
            for (int i = 0; i < 100; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bit);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;
        }
        return data;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        byte b;
        int pixel = bit.getPixel(x, y);
        int red = (pixel & 0x00ff0000) >> 16; // 取高两位
        int green = (pixel & 0x0000ff00) >> 8; // 取中两位
        int blue = pixel & 0x000000ff; // 取低两位
        int gray = RGB2Gray(red, green, blue);
        if (gray < 128) {
            b = 1;
        } else {
            b = 0;
        }
        return b;
    }

    /**
     * 图片灰度的转化
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
        return gray;
    }


}
