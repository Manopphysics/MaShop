package com.example.manop.mashop.Shop;
<<<<<<< HEAD
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Bundle;

import com.example.manop.mashop.Function.Email;
import com.example.manop.mashop.Function.MyCallback;
import com.example.manop.mashop.Function.SaleHistory;
import com.example.manop.mashop.R;


import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.manop.mashop.Decorator.ItemOffsetDecoration;
import com.example.manop.mashop.Product.MyProducts;
import com.example.manop.mashop.Product.Product;
import com.example.manop.mashop.Product.SingleProductActivity;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;
=======

import android.os.Bundle;

import com.example.manop.mashop.R;


import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.manop.mashop.Decorator.ItemOffsetDecoration;
import com.example.manop.mashop.Product.Product;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
<<<<<<< HEAD
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellingHistory extends AppCompatActivity {
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private Button generateSalesHistory;
    private String image;

=======
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellingHistory extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchBtn;
>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
<<<<<<< HEAD
    Bitmap graphbmp = null;
=======

>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_history);
        
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Product");
<<<<<<< HEAD

        generateSalesHistory = (Button) findViewById(R.id.generate_sales_history);

        generateSalesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

        });
=======
        

>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new GridLayoutManager(this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mResultList.addItemDecoration(itemDecoration);
        sellingHistoryList();

<<<<<<< HEAD
        //===========

        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            graphbmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //===========

=======
>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
    }

    private void sellingHistoryList() {
        
        FirebaseRecyclerAdapter<Product, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, HistoryViewHolder>(

                Product.class,
                R.layout.sell_history_item,
                HistoryViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("Shop").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("sell_history")

        ) {
            @Override
            protected void populateViewHolder(HistoryViewHolder viewHolder, Product model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getIMAGE());
                viewHolder.setOrderNum(Integer.toString(position+1));
                viewHolder.setQuantity(model.getQuantity());
                viewHolder.setTotalPrice(Double.toString(Double.parseDouble(model.getPrice())*Double.parseDouble(model.getQuantity())));


            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView order_num,history_product_name,history_product_price,history_product_quantity,history_total_price;
        ImageView history_product_image;
        FirebaseAuth mAuth;
        Context context;
        DatabaseReference mDatabaseLike;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = mView.getContext();
            mAuth = FirebaseAuth.getInstance();

            order_num = (TextView) mView.findViewById(R.id.order_num);
            history_product_name = (TextView) mView.findViewById(R.id.history_product_name);
            history_product_price = (TextView) mView.findViewById(R.id.history_product_price);
            history_product_quantity = (TextView) mView.findViewById(R.id.history_product_quantity);
            history_total_price = (TextView) mView.findViewById(R.id.history_total_price);
            history_product_image = (ImageView) mView.findViewById(R.id.history_product_image);
        }

        public void setPrice(String price) {
<<<<<<< HEAD
            history_product_price.setText("฿"+price);
=======
            history_product_price.setText(price);
>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
        }

        public void setName(String title) {
            history_product_name.setText(title);
        }

        public void setOrderNum(String orderNum){
            order_num.setText(orderNum);
        }

        public void setQuantity(String quan){
            history_product_quantity.setText(quan);
        }

        public void setTotalPrice(String tp){
<<<<<<< HEAD
            history_total_price.setText("฿"+tp);
=======
            history_total_price.setText(tp);
>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
        }

        public void setImage(final Context ctx, final String IMAGE) {
            Glide.with(ctx)
                    .load(IMAGE)
                    .into(history_product_image);

        }

    }
<<<<<<< HEAD

    private void createPdfWrapper() throws FileNotFoundException,DocumentException{

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            try {
                createPdf();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws IOException, DocumentException {



        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("SellingHistory", "Created a new directory for PDF");
        }
        String time = new SimpleDateFormat("dd MM yyyy HH mm ss").format(new Date());
        pdfFile = new File(docsFolder.getAbsolutePath(), "Sales History "+time+".pdf");
        final OutputStream output = new FileOutputStream(pdfFile);
//        Document document = new Document();
//        PdfWriter.getInstance(document, output);
//        document.open();
//        document.add(new Paragraph("TEST BY MANOP"));
//
//        document.close();
//        ArrayList<SaleHistory> saleHistoryList = new ArrayList<SaleHistory>();
//        saleHistoryList.add(new SaleHistory("Pen", 1, 20));
//        saleHistoryList.add(new SaleHistory("Pencil", 2, 10));
        final ArrayList<Product> productList = new ArrayList<Product>();
        readData(new MyCallback() {
            @Override
            public void onCallback(String value) {
            }

            @Override
            public void onCallbackEmailName(String name, String email) {

            }

            @Override
            public void onCallbackProduct(ArrayList<Product> al) {

                double vat = 0.07;
                generateSalesHistoryPdf(al,vat,output);

            }

            @Override
            public void onCallbackSaleHistory(String date, String description, String name, String price, String quantity, String buyerName, String buyerEmail) {

            }

        });

        previewPdf();


    }
    private void generateSalesHistoryPdf(ArrayList<Product> productList, double vat, OutputStream output ){
        Document document = new Document();


        Paragraph space = new Paragraph("\n");

        PdfPTable headder = new PdfPTable(new float[]{1, 4});

        Paragraph purchaseOrder = new Paragraph("Sales History\n", new Font(Font.FontFamily.TIMES_ROMAN, 15));
        purchaseOrder.setAlignment(Element.ALIGN_RIGHT);
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH.mm").format(new Date());
        Chunk date = new Chunk("\tDate: " + timeStamp + "\n", new Font(Font.FontFamily.TIMES_ROMAN, 10));
        Chunk no = new Chunk("\tPO Number. _______________\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 10));
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

        PdfPTable table = new PdfPTable(new float[]{2, 4, 8, 3, 4});
        table.setWidthPercentage(105);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Order Number");
        table.addCell("Product Name");
        table.addCell("Quantity");
        table.addCell("Unit Price");
        table.addCell("Total Price");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for (int i = 0; i < productList.size(); i++) {
            Paragraph item = new Paragraph(Integer.toString(i + 1), new Font(Font.FontFamily.TIMES_ROMAN, 12));
            Paragraph productName = new Paragraph(productList.get(i).getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12));
            Paragraph quantity = new Paragraph(productList.get(i).getQuantity(), new Font(Font.FontFamily.TIMES_ROMAN, 12));
            Paragraph unitPrice = new Paragraph(productList.get(i).getPrice(), new Font(Font.FontFamily.TIMES_ROMAN, 12));
            Paragraph price = new Paragraph(Double.toString(Double.parseDouble(productList.get(i).getPrice())*
                    Double.parseDouble(productList.get(i).getQuantity())), new Font(Font.FontFamily.TIMES_ROMAN, 12));

            table.addCell(item);
            table.addCell(productName);
            table.addCell(quantity);
            table.addCell(unitPrice);
            table.addCell(price);
        }

        PdfPCell blankCell = new PdfPCell();
        blankCell.setBorder(0);

        table.addCell(blankCell);
//        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);

        double sub_total = 0;
        for (Product x : productList) {
            sub_total += Double.parseDouble(x.getPrice())*
                    Double.parseDouble(x.getQuantity());
        }
        table.addCell(new Paragraph("Sub total:", new Font(Font.FontFamily.TIMES_ROMAN, 14)));
        table.addCell(new Paragraph(Double.toString(sub_total), new Font(Font.FontFamily.TIMES_ROMAN, 14)));

//        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);
        table.addCell(blankCell);

///////////////////// edited ////////////////////////////////////////////////////////////////
        table.addCell(new Paragraph("Tax:", new Font(Font.FontFamily.TIMES_ROMAN, 14)));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String numberAsString = decimalFormat.format(sub_total * vat);
        table.addCell(new Paragraph(numberAsString+" (7%)", new Font(Font.FontFamily.TIMES_ROMAN, 14)));
//////////////////////////////////////////////////////////////////////////////////////////////
        PdfPTable signatureTable = new PdfPTable(new float[]{1, 1});
        signatureTable.setWidthPercentage(105);
        signatureTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        signatureTable.getDefaultCell().setBorder(0);

//        Chunk sellerSignature = new Chunk("Seller:___________________\n", new Font(Font.FontFamily.TIMES_ROMAN, 10));
//        Chunk sellerName = new Chunk("Name:" + SellerName + "\n", new Font(Font.FontFamily.TIMES_ROMAN, 10));
//        Paragraph seller = new Paragraph();
//        seller.add(sellerSignature);
//        seller.add(sellerName);
//        seller.setAlignment(Element.ALIGN_LEFT);

//        signatureTable.addCell(seller);

        try {
            PdfWriter.getInstance(document, output);
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            graphbmp.compress(Bitmap.CompressFormat.PNG, 100 , stream);
            Image myImg = null;
            try {
                myImg = Image.getInstance(stream.toByteArray());
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myImg.setAlignment(Image.LEFT);
            myImg.scaleAbsolute(150,150);
            document.add(myImg);
            document.add(headder);
            document.add(space);
            document.add(headder2);
            document.add(space);
            document.add(table);
            document.add(space);
            document.add(signatureTable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            document.close();
        }catch(Exception e){e.printStackTrace();}
    }
    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkURI = FileProvider.getUriForFile(
                    SellingHistory.this,
                    this.getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }
    public void readData(final MyCallback mc){
        FirebaseDatabase.getInstance().getReference().child("Shop").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("sell_history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Product> al = new ArrayList<Product>();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    al.add(ds.getValue(Product.class));
                }
                mc.onCallbackProduct(al);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

=======
>>>>>>> 03ad702b609c7d3d5217181358554732c2b3f46f
}

