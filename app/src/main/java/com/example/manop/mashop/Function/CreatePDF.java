package com.example.manop.mashop.Function;

/**
 * Created by Manop on 11/24/2018.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;

        import com.example.manop.mashop.Product.Product;
        import com.itextpdf.text.Font.FontFamily;
        import com.itextpdf.text.*;
        import com.itextpdf.text.pdf.*;
        import com.itextpdf.text.Image;
        import com.itextpdf.text.pdf.PdfWriter;
        import java.io.IOException;
        import java.text.DecimalFormat;
        import java.util.ArrayList;

public class CreatePDF {

    public static void generateSalesAccount(String ShopName, String ShopAddress, String PhoneNumber,
                                            String Date, String SellerName, String BuyerName, double vat, ArrayList<SaleHistory> saleHistoryList)
            throws FileNotFoundException, DocumentException, BadElementException, IOException {
        Document document = new Document();


        Chunk shopName = new Chunk("\t" + ShopName + "\n", new Font(FontFamily.TIMES_ROMAN, 18));
        Chunk shopAddress = new Chunk("\t" + ShopAddress + "\n", new Font(FontFamily.TIMES_ROMAN, 12));
        Chunk phoneNumber = new Chunk("\t" + PhoneNumber + "\n\n", new Font(FontFamily.TIMES_ROMAN, 12));

        Paragraph parHeadder = new Paragraph();
        parHeadder.add(shopName);
        parHeadder.add(shopAddress);
        parHeadder.add(phoneNumber);
        parHeadder.setAlignment(Element.ALIGN_CENTER);

        Paragraph space = new Paragraph("\n");

        PdfPTable headder = new PdfPTable(new float[]{1, 4});
        headder.addCell(parHeadder);

        Paragraph SaleAccount = new Paragraph("Sale Account\n", new Font(FontFamily.TIMES_ROMAN, 15));
        SaleAccount.setAlignment(Element.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(new float[]{1, 3, 1});
        table.setWidthPercentage(105);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell("Date");
        table.addCell("Particulars");
        table.addCell("Amount");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        double total_amount = 0;
        for (int i = 0; i < saleHistoryList.size(); i++) {
            Chunk productName = new Chunk("    " + saleHistoryList.get(i).getProductName()+ "\n", new Font(FontFamily.TIMES_ROMAN, 12));
            Chunk quantity = new Chunk("    Amount: " + Integer.toString(saleHistoryList.get(i).getQuantity()) + " pcs.\n\n", new Font(FontFamily.TIMES_ROMAN, 12));

            Paragraph particulars = new Paragraph();
            particulars.add(productName);
            particulars.add(quantity);


            table.addCell(particulars);
            table.addCell(new Paragraph(Double.toString(saleHistoryList.get(i).getTotalprice())));
            total_amount += saleHistoryList.get(i).getTotalprice();
        }

        PdfPCell blankCell = new PdfPCell();
        blankCell.setBorder(0);
        table.addCell(blankCell);

        Paragraph totally = new Paragraph("Totally:", new Font(FontFamily.TIMES_ROMAN, 13));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph amount = new Paragraph(Double.toString(total_amount), new Font(FontFamily.TIMES_ROMAN, 13));
        table.addCell(totally);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(amount);

        PdfWriter.getInstance(document, new FileOutputStream("sample4.pdf"));
        document.open();

        document.add(headder);
        document.add(space);

        document.add(SaleAccount);
        document.add(space);
        document.add(table);

        document.close();
        System.out.println("Done");

    }

    public static void generatePerchaseOrder(String ShopName, String ShopAddress, String PhoneNumber, String image,
                                             String Date, String SellerName, String BuyerName, double vat, ArrayList<Product> productList)
            throws FileNotFoundException, DocumentException, BadElementException, IOException {
        Document document = new Document();

        Image logoImage = Image.getInstance(image);
        logoImage.scaleAbsolute(50f, 50f);

        Chunk shopName = new Chunk("\t" + ShopName + "\n", new Font(FontFamily.TIMES_ROMAN, 18));
        Chunk shopAddress = new Chunk("\t" + ShopAddress + "\n", new Font(FontFamily.TIMES_ROMAN, 12));
        Chunk phoneNumber = new Chunk("\t" + PhoneNumber + "\n\n", new Font(FontFamily.TIMES_ROMAN, 12));

        Paragraph parHeadder = new Paragraph();
        parHeadder.add(shopName);
        parHeadder.add(shopAddress);
        parHeadder.add(phoneNumber);
        parHeadder.setAlignment(Element.ALIGN_CENTER);

        Paragraph space = new Paragraph("\n");

        PdfPTable headder = new PdfPTable(new float[]{1, 4});
        headder.addCell(logoImage);
        headder.addCell(parHeadder);

        Paragraph purchaseOrder = new Paragraph("PURCHASE ORDER\n", new Font(FontFamily.TIMES_ROMAN, 15));
        purchaseOrder.setAlignment(Element.ALIGN_RIGHT);

        Chunk date = new Chunk("\tDate: " + Date + "\n", new Font(FontFamily.TIMES_ROMAN, 10));
        Chunk no = new Chunk("\tPO Number. _______________\n\n", new Font(FontFamily.TIMES_ROMAN, 10));
        Paragraph dateAndNo = new Paragraph();
        dateAndNo.setAlignment(Element.ALIGN_RIGHT);
        dateAndNo.add(no);
        dateAndNo.add(date);

        PdfPTable headder2 = new PdfPTable(new float[]{3, 2, 2});
        headder2.setWidthPercentage(125);
        headder2.getDefaultCell().setBorder(0);
        headder2.addCell(new Paragraph(""));
        headder2.addCell(purchaseOrder);
        headder2.addCell(dateAndNo);

        PdfPTable table = new PdfPTable(new float[]{2, 4, 8, 3, 4, 4});
        table.setWidthPercentage(105);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("ITEM");
        table.addCell("PRODUCT ID");
        table.addCell("PRODUCT NAME");
        table.addCell("QUANTITY");
        table.addCell("UNIT PRICE");
        table.addCell("PRICE");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for (int i = 0; i < productList.size(); i++) {
            Paragraph item = new Paragraph(Integer.toString(i + 1), new Font(FontFamily.TIMES_ROMAN, 12));
            Paragraph productName = new Paragraph(productList.get(i).getName(), new Font(FontFamily.TIMES_ROMAN, 12));
            Paragraph quantity = new Paragraph(productList.get(i).getQuantity(), new Font(FontFamily.TIMES_ROMAN, 12));
            Paragraph unitPrice = new Paragraph(productList.get(i).getPrice(), new Font(FontFamily.TIMES_ROMAN, 12));
            Paragraph price = new Paragraph(Double.toString(Double.parseDouble(productList.get(i).getPrice())*
                    Double.parseDouble(productList.get(i).getQuantity())), new Font(FontFamily.TIMES_ROMAN, 12));

            table.addCell(item);
            table.addCell(productName);
            table.addCell(quantity);
            table.addCell(unitPrice);
            table.addCell(price);
        }

        PdfPCell blankCell = new PdfPCell();
        blankCell.setBorder(0);

        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);

        double sub_total = 0;
        for (Product x : productList) {
            sub_total += Double.parseDouble(x.getPrice())*
                    Double.parseDouble(x.getQuantity());
        }
        table.addCell(new Paragraph("Sub total:", new Font(FontFamily.TIMES_ROMAN, 14)));
        table.addCell(new Paragraph(Double.toString(sub_total), new Font(FontFamily.TIMES_ROMAN, 14)));

        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);

///////////////////// edited ////////////////////////////////////////////////////////////////
        table.addCell(new Paragraph("Tax:", new Font(FontFamily.TIMES_ROMAN, 14)));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String numberAsString = decimalFormat.format(sub_total * vat);
        table.addCell(new Paragraph(numberAsString, new Font(FontFamily.TIMES_ROMAN, 14)));
//////////////////////////////////////////////////////////////////////////////////////////////
        PdfPTable signatureTable = new PdfPTable(new float[]{1, 1});
        signatureTable.setWidthPercentage(105);
        signatureTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        signatureTable.getDefaultCell().setBorder(0);

        Chunk sellerSignature = new Chunk("Seller:___________________\n", new Font(FontFamily.TIMES_ROMAN, 10));
        Chunk sellerName = new Chunk("Name:" + SellerName + "\n", new Font(FontFamily.TIMES_ROMAN, 10));
        Paragraph seller = new Paragraph();
        seller.add(sellerSignature);
        seller.add(sellerName);
        seller.setAlignment(Element.ALIGN_LEFT);

        Chunk buyerSignature = new Chunk("Buyer:___________________\n", new Font(FontFamily.TIMES_ROMAN, 10));
        Chunk buyerName = new Chunk("Name:" + BuyerName + "\n", new Font(FontFamily.TIMES_ROMAN, 10));
        Paragraph buyer = new Paragraph();
        buyer.add(buyerSignature);
        buyer.add(buyerName);
        buyer.setAlignment(Element.ALIGN_RIGHT);

        signatureTable.addCell(seller);
        signatureTable.addCell(buyer);

        PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
        document.open();

        document.add(headder);
        document.add(space);
        document.add(headder2);
        document.add(space);
        document.add(table);
        document.add(space);
        document.add(signatureTable);

        document.close();
        System.out.println("Done");

    }

//    public static void main(String[] args) throws DocumentException, BadElementException, IOException {
////        ArrayList<Product> productlist = new ArrayList<Product>();
////        productlist.add(new Product(111, "diewdo", 1, 200));
////        productlist.add(new Product(222, "doll", 2, 100));
//
//
//        ArrayList<SaleHistory> saleHisLst = new ArrayList<SaleHistory>();
//        saleHisLst.add(new SaleHistory("Pen", 1, 20));
//        saleHisLst.add(new SaleHistory("Pencil", 2, 10));
//        generateSalesAccount("Mashop", "991 Rama 1 Road, Pathumwan Bangkok 10330", "+66 2 610 8000",
//                "1/1/1111", "Somchai", "Sommai", 0.07, saleHisLst);
//
//    }
}
