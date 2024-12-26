package com.example.logiXpert.service;

import com.example.logiXpert.dto.GetShipmentDto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class PdfGeneratorServiceImpl {

    private byte[] generateBarcode(String text) throws WriterException, IOException {
        BitMatrix bitMatrix = new com.google.zxing.oned.Code128Writer()
                .encode(text, BarcodeFormat.CODE_128, 300, 100);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    private byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public byte[] generateShipmentInvoice(GetShipmentDto shipment) throws WriterException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Paragraph title = new Paragraph("Shipment Invoice")
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY);
        document.add(title);

        Table mainTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1));

        Table shipmentInfoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100));
        shipmentInfoTable.addCell("Tracking Number:").addCell(shipment.trackingNumber());
        shipmentInfoTable.addCell("Shipment Date:").addCell(shipment.shipmentDate().format(dateFormatter));
        shipmentInfoTable.addCell("Sender:").addCell(shipment.sender().name());
        shipmentInfoTable.addCell("Receiver:").addCell(shipment.receiver().name());
        shipmentInfoTable.addCell("Price:").addCell(String.format("%.2f BGN", shipment.price()));
        shipmentInfoTable.addCell("Weight:").addCell(String.format("%.1f kg", shipment.weight()));
        shipmentInfoTable.addCell("Destination:").addCell(shipment.destination());

        mainTable.addCell(shipmentInfoTable);

        Table companyInfoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100));
        companyInfoTable.addCell("Company Name:").addCell(new Paragraph("LogiXpert Inc.").setBold().setFontSize(14));
        companyInfoTable.addCell("Company Phone:").addCell("+359 88 123 4567");

        Table qrBarcodeTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        Cell titleCell = new Cell()
                .add(new Paragraph("Tracking Barcode and QR Code")
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(10)
                        .setMarginBottom(3))
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1));
        qrBarcodeTable.addCell(titleCell);

        Table barcodeAndQRCode = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));

        byte[] barcodeBytes = generateBarcode(shipment.trackingNumber());
        ImageData barcodeData = ImageDataFactory.create(barcodeBytes);
        Image barcode = new Image(barcodeData)
                .setWidth(80)
                .setHeight(40)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        byte[] qrCodeBytes = generateQRCode("http://localhost:8080/shipment/" + shipment.trackingNumber(), 100, 100);
        ImageData qrCodeData = ImageDataFactory.create(qrCodeBytes);
        Image qrCode = new Image(qrCodeData)
                .setWidth(90)
                .setHeight(90)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        barcodeAndQRCode.addCell(new Cell().add(barcode).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        barcodeAndQRCode.addCell(new Cell().add(qrCode).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        qrBarcodeTable.addCell(new Cell().add(barcodeAndQRCode).setBorder(Border.NO_BORDER));

        companyInfoTable.addCell(new Cell(1, 2).add(qrBarcodeTable).setBorder(Border.NO_BORDER));

        mainTable.addCell(companyInfoTable);

        document.add(mainTable);

        Paragraph footer = new Paragraph("Thank you for using LogiXpert!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setMarginTop(20)
                .setFontColor(ColorConstants.GRAY);
        document.add(footer);

        document.close();
        return outputStream.toByteArray();
    }
}