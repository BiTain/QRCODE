package com.bitain.qrcode.utils;

import com.bitain.qrcode.exception.ComponentException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class QrCodeUtil {
    @Value("${qrCode.width}")
    private int wid;
    @Value("${qrCode.height}")
    private int hei;
    public String generateQrCode(String attendanceType){
        StringBuilder result = new StringBuilder();
        if (attendanceType!=null){
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(attendanceType + parseDate(new Date()), BarcodeFormat.QR_CODE,wid,hei);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage,"png",os);
                result.append(new String(Base64.getEncoder().encode(os.toByteArray())));
            } catch (WriterException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }

    public String validateQrCode(String base64EncodedImage){
        byte[] imageBytes = Base64.getDecoder().decode(base64EncodedImage);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Thiết lập các thông số cần thiết để giải mã
            MultiFormatReader reader = new MultiFormatReader();
            com.google.zxing.Reader qrCodeReader = new MultiFormatReader();
            java.util.Hashtable<DecodeHintType, Object> hints = new java.util.Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            // Giải mã mã QR và trả về chuỗi kết quả
            Result result = reader.decode(bitmap, hints);
            String typeAttendace = null;
            if(result.getText().contains(parseDate(new Date()))){
                typeAttendace = result.getText().replace(parseDate(new Date()),"");
            }else {
                throw new ComponentException("Timekeeping date does not match", HttpStatus.BAD_REQUEST);
            }
            return typeAttendace;
        } catch (IOException | NotFoundException e) {
            // Xử lý các ngoại lệ nếu có
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        QrCodeUtil qrCodeUtil = new QrCodeUtil();
//        System.out.println(qrCodeUtil.generateQrCode("checkout"));
//        //System.out.println(qrCodeUtil.validateQrCode("iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAABlUlEQVR4Xu3ZMZKDMAwFUGcoKDkCR+FoydE4io9ASZFBqy8ZkpjM7I7Fdl8F61iPxvIqhiT5SyypnvkaZFWQVUFWBVkVZFU4W5PHYIP7PMrabWWuIwszG0secNXUqH82r4+nyGIMaz4pe97kqII8UBeyC5kufrpbOcj+iWmQXctwxeL7Jkd2s0xJkYWYLb2tORZfy+F1QZCF2RFruomxj1myGEPWe8gTTNe+Rw+x1EexyNpYwt7GpGWzfnZfykEWYRY6mXqrwpS/V4GsjVkV3iInq4LM6dTJyRqY7W1Mmp9RDhuMS2/3kcWYYJOP4qfiSSyrTdsMWZTZ8k+5LL5ggHMy8tX/AlkD03F5IyGvXm33ohxkcYYvvqEc5KTUBYEUWYgdgV6Nvb3f6M2ELMb2NffD8OP1EF08WYzZWLI+3Cl+ayajzZNFGd7zoId03quXUpbiya5jZfHRTODJrmPltfC4H4/z6eGOrIXhKtl/UNa9veCcrD6RXcE8m7yHgOHT+RuQrIn9GmRVkFVBVgVZFWRVXMx+AGDRvFu9VVbWAAAAAElFTkSuQmCC"));
//    }

    private String parseDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
