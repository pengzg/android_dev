package com.xdjd.utils.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.github.promeg.pinyinhelper.Pinyin;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * 蓝牙打印工具类
 */
public class PrintUtil {

    private OutputStreamWriter mWriter = null;
    private OutputStream mOutputStream = null;

    public static int WIDTH_PIXEL = 573;//382;

    public static int IMAGE_SIZE = 240;

    /**
     * 初始化Pos实例
     *
     * @param encoding 编码
     * @throws IOException
     */
    public PrintUtil(OutputStream outputStream, String encoding) throws IOException {
        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
            WIDTH_PIXEL = 573;
            LINE_BYTE_SIZE = 48;

            IMAGE_SIZE = 360;

            ThreeLeft = 16;
            ThreeMiddle = 16;
            ThreeRight = 16;

            LEFT_ONE = 16;
            LEFT_TWO = 10;
            RIGHT_TWO = 13;
            RIGHT_ONE = 9;
        } else {//50格式宽度初始化
            WIDTH_PIXEL = 382;
            LINE_BYTE_SIZE = 32;

            IMAGE_SIZE = 360;

            ThreeLeft = 10;
            ThreeMiddle = 13;
            ThreeRight = 9;

            LEFT_ONE = 4;
            LEFT_TWO = 10;
            RIGHT_TWO = 11;
            RIGHT_ONE = 7;
        }

        mWriter = new OutputStreamWriter(outputStream, encoding);
        mOutputStream = outputStream;
        initPrinter();
    }

    /**
     * 打印纸一行最大的字节
     */
    private static int LINE_BYTE_SIZE = 48;

    /**
     * 表头--xxxx:+空格,字符长度
     */
    private static int OneFormatLenght = 12;

    //----------------------三列宽度参数--------
    private static int ThreeLeft = 16;
    private static int ThreeMiddle = 16;
    private static int ThreeRight = 16;

    //----------------------四列宽度参数-------------
    //左侧第一个参数字符长度        120.00 /箱    1
    private static int LEFT_ONE = 16;
    //左侧第二个参数
    private static int LEFT_TWO = 10;
    //右侧第二个参数
    private static int RIGHT_TWO = 13;
    //最右侧参数
    private static int RIGHT_ONE = 9;

    /**
     * 文字加粗打印
     *
     * @throws IOException
     */
    public void printTextBold() throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x45);
        mWriter.write(0x01);

        mWriter.flush();
    }

    /**
     * 取消加粗模式
     *
     * @throws IOException
     */
    public void printTextBoldCancel() throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x45);
        mWriter.write(0x00);

        mWriter.flush();
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public void printThreeData(String leftText, String middleText, String rightText) throws IOException {
        StringBuilder sb = new StringBuilder();

        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        if (leftTextLength < ThreeLeft) {//当最左侧文字小于LEFT_ONE80的最大长度,添加缺少的空格
            for (int i = 0; i < ThreeLeft - leftTextLength; i++) {
                sb.append(" ");
            }
        } else if (leftTextLength > ThreeLeft) {
            for (int i = 0; i < ThreeLeft + LINE_BYTE_SIZE - leftTextLength; i++) {
                sb.append(" ");
            }
        }

        sb.append(middleText);
        if (middleTextLength < ThreeMiddle) {
            for (int i = 0; i < ThreeMiddle - middleTextLength; i++) {
                sb.append(" ");
            }
        }else if (middleTextLength == ThreeMiddle){
            sb.append(" ");
        }else if (middleTextLength > ThreeMiddle){
            sb.append(" ");
        }

        sb.append(rightText);
        if (rightTextLength < ThreeRight) {
            for (int i = 0; i < ThreeRight - rightTextLength; i++) {
                sb.append(" ");
            }
        }

        sb.append("\n");
        print(getGbk(sb.toString()));
    }

    /**
     * 打印四列--超出数据进行换行
     *
     * @param leftOneText  左侧第一行文字
     * @param leftTwoText  左侧第二行文字
     * @param rightTwoText 右侧第二行文字
     * @param rightOneText 右侧第一行文字
     * @return
     */
    @SuppressLint("NewApi")
    public void printFourData(String leftOneText, String leftTwoText, String rightTwoText, String rightOneText) throws IOException {
        StringBuilder sb = new StringBuilder();
        leftOneText = leftOneText + " ";

        int leftOneTextLength = getBytesLength(leftOneText);
        int leftTwoTextLength = getBytesLength(leftTwoText);
        int rightTwoTextLength = getBytesLength(rightTwoText);
        int rightOneTextLength = getBytesLength(rightOneText);

        sb.append(leftOneText);
        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
            if (leftOneTextLength < LEFT_ONE) {//当最左侧文字小于LEFT_ONE80的最大长度,添加缺少的空格
                for (int i = 0; i < LEFT_ONE - leftOneTextLength; i++) {
                    sb.append(" ");
                }
            } else if (leftOneTextLength > LEFT_ONE) {
                for (int i = 0; i < LEFT_ONE + LINE_BYTE_SIZE - leftOneTextLength; i++) {
                    sb.append(" ");
                }
            }
        } else {
            for (int i = 0; i < LEFT_ONE + LINE_BYTE_SIZE - leftOneTextLength; i++) {
                sb.append(" ");
            }
        }

        sb.append(leftTwoText);
        if (leftTwoTextLength < LEFT_TWO) {
            for (int i = 0; i < LEFT_TWO - leftTwoTextLength; i++) {
                sb.append(" ");
            }
        }else{
            sb.append(" ");
        }

        sb.append(rightTwoText);
        if (rightTwoTextLength < RIGHT_TWO) {
            for (int i = 0; i < RIGHT_TWO - rightTwoTextLength; i++) {
                sb.append(" ");
            }
        }else{
            sb.append(" ");
        }

        sb.append(rightOneText);
        if (rightOneTextLength < RIGHT_ONE) {
            for (int i = 0; i < RIGHT_ONE - rightOneTextLength; i++) {
                sb.append(" ");
            }
        }

        sb.append("\n");
        print(getGbk(sb.toString()));
    }

    public void print(byte[] bs) throws IOException {
        mOutputStream.write(bs);
    }

    public void printRawBytes(byte[] bytes) throws IOException {
        mOutputStream.write(bytes);
        mOutputStream.flush();
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    public void initPrinter() throws IOException {
        mWriter.write(0x1B);
        mWriter.write(0x40);
        mWriter.flush();
    }

    /**
     * 打印换行
     *
     * @return length 需要打印的空行数
     * @throws IOException
     */
    public void printLine(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            mWriter.write("\n");
        }
        mWriter.flush();
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    public void printLine() throws IOException {
        printLine(1);
    }

    public void printFlush() throws IOException {
        mWriter.flush();
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    public void printTabSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            mWriter.write("\t");
        }
        mWriter.flush();
    }

    /**
     * 绝对打印位置
     *
     * @return
     * @throws IOException
     */
    public byte[] setLocation(int offset) throws IOException {
        byte[] bs = new byte[4];
        bs[0] = 0x1B;
        bs[1] = 0x24;
        bs[2] = (byte) (offset % 256);
        bs[3] = (byte) (offset / 256);
        return bs;
    }

    public byte[] getGbk(String stText) throws IOException {
        byte[] returnText = stText.getBytes("GBK"); // 必须放在try内才可以
        return returnText;
    }

    private int getStringPixLength(String str) {
        int pixLength = 0;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Pinyin.isChinese(c)) {
                pixLength += 24;
            } else {
                pixLength += 12;
            }
        }
        return pixLength;
    }

    public int getOffset(String str) {
        return WIDTH_PIXEL - getStringPixLength(str);
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    public void printText(String text) throws IOException {
        mWriter.write(text);
        mWriter.flush();
    }

    /**
     * 打印文字,换行
     * @param text
     * @throws IOException
     */
    public void printTextEnter(String text) throws IOException {
        mWriter.write(text);
        mWriter.write("\n");
        mWriter.flush();
    }

    /**
     * 复位打印机
     */
    public void printReset() throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x40);
        mWriter.flush();
    }

    /**
     * 对齐0:左对齐，1：居中，2：右对齐
     */
    public void printAlignment(int alignment) throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x61);
        mWriter.write(alignment);
        mWriter.flush();
    }

    /**
     * 中间打印标题
     * @param text
     * @param alignment 对齐0:左对齐，1：居中，2：右对齐
     * @throws IOException
     */
    public void printLargeText(String text,int alignment) throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x61);
        mWriter.write(alignment);

        mWriter.write(0x1d);
        mWriter.write(0x21);
        mWriter.write(0x01);

        mWriter.write(text);
        mWriter.flush();

        mWriter.write(0x1d);
        mWriter.write(0x21);
        mWriter.write(0x00);
        mWriter.flush();
    }

    /**
     * 表头格式打印--xxxx:  xx
     * @param leftText
     * @param rightText
     * @throws IOException
     */
    public void printTwoFormatOne(String leftText, String rightText) throws IOException {
        StringBuilder sb = new StringBuilder();

        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        if (leftTextLength < OneFormatLenght) {//当最左侧文字小于LEFT_ONE80的最大长度,添加缺少的空格
            for (int i = 0; i < OneFormatLenght - leftTextLength; i++) {
                sb.append(" ");
            }
        }

        sb.append(rightText);

        sb.append("\n");
        print(getGbk(sb.toString()));
    }

    /**
     * 两列(左右对齐样式)
     * @param leftText
     * @param rightText
     * @throws IOException
     */
    public void printTwoColumn(String leftText, String rightText) throws IOException {
        StringBuilder sb = new StringBuilder();

        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        //左侧总打印长度
        int leftTotalLenght = LINE_BYTE_SIZE-rightTextLength;

        sb.append(leftText);
        if (leftTextLength < leftTotalLenght) {//总宽度减去右侧文字长度,得到左侧文字可打印的宽度
            for (int i = 0; i < leftTotalLenght - leftTextLength; i++) {
                sb.append(" ");
            }
        } else if (leftTextLength > leftTotalLenght) {
            sb.append(" ");
        }

        sb.append(rightText);

        sb.append("\n");
        print(getGbk(sb.toString()));
    }

    /**
     * 两列(左中对齐格式)
     * @param leftText
     * @param rightText
     * @throws IOException
     */
    public void printTwoColumnWeight(String leftText, String rightText) throws IOException {
        StringBuilder sb = new StringBuilder();

        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        if (leftTextLength < LINE_BYTE_SIZE/2) {//当最左侧文字小于LEFT_ONE80的最大长度,添加缺少的空格
            for (int i = 0; i < LINE_BYTE_SIZE/2 - leftTextLength; i++) {
                sb.append(" ");
            }
        } else if (leftTextLength > LINE_BYTE_SIZE/2) {
            for (int i = 0; i < LINE_BYTE_SIZE - leftTextLength; i++) {
                sb.append(" ");
            }
        }

        sb.append(rightText);

        sb.append("\n");
        print(getGbk(sb.toString()));
    }

    public void printDashLine() throws IOException {
        if (UserInfoUtils.getPtinterType(UIUtils.getContext()) == BaseConfig.printer80) {
            printText("------------------------------------------------");
        } else {
            printText("--------------------------------");
        }
    }

    public void printDashText(String title) throws IOException {
        StringBuilder sb = new StringBuilder();

        int titleLenght = getBytesLength(title);
        int lenght = (LINE_BYTE_SIZE - titleLenght-2)/2;

        for (int i = 0; i < lenght; i++){
            sb.append("-");
        }

        sb.append(" "+title+" ");

        for (int i = 0; i < LINE_BYTE_SIZE - lenght - 2 - titleLenght; i++){
            sb.append("-");
        }
        sb.append("\n");
        print(getGbk(sb.toString()));
    }

    /**
     * 打印条码
     *
     * @param barcode
     * @return
     */
    public static String getBarcodeCmd(String barcode) {
        // 打印 Code-128 条码时需要使用字符集前缀
        // "{A" 表示大写字母
        // "{B" 表示所有字母，数字，符号
        // "{C" 表示数字，可以表示 00 - 99 的范围
        byte[] data;

        String btEncode = barcode;

        /*if (barcode.length() < 18) {
            // 字符长度小于15的时候直接输出字符串
            btEncode = "{B" + barcode;
        } else {
            // 否则做一点优化
            int startPos = 0;
            btEncode = "{B";

            for (int i = 0; i < barcode.length(); i++) {
                char curChar = barcode.charAt(i);
                if (curChar < 48 || curChar > 57 || i == (barcode.length() - 1)) {
                    // 如果是非数字或者是最后一个字符
                    if (i - startPos >= 10) {
                        if (startPos == 0) {
                            btEncode = "";
                        }
                        btEncode += "{C";
                        boolean isFirst = true;
                        int numCode = 0;

                        for (int j = startPos; j < i; j++) {

                            if (isFirst) { // 处理第一位
                                numCode = (barcode.charAt(j) - 48) * 10;
                                isFirst = false;
                            } else { // 处理第二位
                                numCode += (barcode.charAt(j) - 48);
                                btEncode += (char) numCode;
                                isFirst = true;
                            }
                        }
                        btEncode += "{B";
                        if (!isFirst) {
                            startPos = i - 1;
                        } else {
                            startPos = i;
                        }
                    }
                    for (int k = startPos; k <= i; k++) {
                        btEncode += barcode.charAt(k);
                    }
                    startPos = i + 1;
                }
            }
        }*/

        //[0x1D, 0x6B, 0x49, 0x0E, 0x7B, 0x43]

        // 设置 HRI 的位置，02 表示下方
        byte[] hriPosition = {(byte) 0x1d, (byte) 0x48, (byte) 0x02};
        // 最后一个参数表示宽度 取值范围 1-6 如果条码超长则无法打印
        byte[] width = {(byte) 0x1d, (byte) 0x77, (byte) 0x01};
        byte[] height = {(byte) 0x1d, (byte) 0x68, (byte) 0x30};
        byte[] center = {(byte) 0x1B, (byte) 0x61, (byte) 0x01};
        // 最后两个参数 73 ： CODE 128 || 编码的长度
        byte[] barcodeType = {(byte) 0x1d, (byte) 0x6b, (byte) 73, (byte) btEncode.length()};
        byte[] print = {(byte) 10, (byte) 0};

        data = byteMerger(hriPosition, width);
        data = byteMerger(data, height);
        data = byteMerger(data, center);
        data = byteMerger(data, barcodeType);
        data = byteMerger(data, btEncode.getBytes());
        data = byteMerger(data, print);

        return new String(data);
    }

    public static String getGoodsCode(String barcode) {
        // 打印 Code-128 条码时需要使用字符集前缀
        // "{A" 表示大写字母
        // "{B" 表示所有字母，数字，符号
        // "{C" 表示数字，可以表示 00 - 99 的范围
        byte[] data;

        String btEncode = barcode;

        /*if (barcode.length() < 18) {
            // 字符长度小于15的时候直接输出字符串
            btEncode = "{B" + barcode;
        } else {
            // 否则做一点优化
            int startPos = 0;
            btEncode = "{B";

            for (int i = 0; i < barcode.length(); i++) {
                char curChar = barcode.charAt(i);
                if (curChar < 48 || curChar > 57 || i == (barcode.length() - 1)) {
                    // 如果是非数字或者是最后一个字符
                    if (i - startPos >= 10) {
                        if (startPos == 0) {
                            btEncode = "";
                        }
                        btEncode += "{C";
                        boolean isFirst = true;
                        int numCode = 0;

                        for (int j = startPos; j < i; j++) {

                            if (isFirst) { // 处理第一位
                                numCode = (barcode.charAt(j) - 48) * 10;
                                isFirst = false;
                            } else { // 处理第二位
                                numCode += (barcode.charAt(j) - 48);
                                btEncode += (char) numCode;
                                isFirst = true;
                            }
                        }
                        btEncode += "{B";
                        if (!isFirst) {
                            startPos = i - 1;
                        } else {
                            startPos = i;
                        }
                    }
                    for (int k = startPos; k <= i; k++) {
                        btEncode += barcode.charAt(k);
                    }
                    startPos = i + 1;
                }
            }
        }
*/
        // 设置 HRI 的位置，02 表示下方
        byte[] hriPosition = {(byte) 0x1d, (byte) 0x48, (byte) 0x02};
        byte[] textStyle = {(byte) 0x1d, (byte) 0x66, (byte) 0x02};
        // 最后一个参数表示宽度 取值范围 1-6 如果条码超长则无法打印
        byte[] width = {(byte) 0x1d, (byte) 0x77, (byte) 0x03};
        byte[] height = {(byte) 0x1d, (byte) 0x68, (byte) 0x25};
        byte[] center = {(byte) 0x1B, (byte) 0x61, (byte) 0x01};
        // 最后两个参数 73 ： CODE 128 || 编码的长度
        byte[] barcodeType = {(byte) 0x1d, (byte) 0x6b, (byte) 4, (byte) btEncode.length()};
        byte[] print = {(byte) 10, (byte) 0};

        data = byteMerger(hriPosition, width);
//        data = byteMerger(textStyle, width);
        data = byteMerger(data, height);
        data = byteMerger(data, center);
        data = byteMerger(data, barcodeType);
        data = byteMerger(data, btEncode.getBytes());
        data = byteMerger(data, print);

        return new String(data);
    }

    /**
     * 字节数组合并
     *
     * @param bytesA
     * @param bytesB
     * @return
     */
    public static byte[] byteMerger(byte[] bytesA, byte[] bytesB) {
        byte[] bytes = new byte[bytesA.length + bytesB.length];
        System.arraycopy(bytesA, 0, bytes, 0, bytesA.length);
        System.arraycopy(bytesB, 0, bytes, bytesA.length, bytesB.length);
        return bytes;
    }

    public static void printTest(BluetoothSocket bluetoothSocket, Bitmap bitmap) {
        PrintUtil pUtil = null;
        try {
            pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            pUtil.printBitmap(bitmap);
            pUtil.printLine(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printBitmap(Bitmap bmp) throws IOException {
        bmp = compressPic(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        printRawBytes(bmpByteArray);
    }

    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    private byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;

        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    private byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
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
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
        return gray;
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    private Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = IMAGE_SIZE;
        int newHeight = 180;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }


}