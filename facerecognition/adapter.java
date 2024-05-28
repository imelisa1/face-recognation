package com.atharvakale.facerecognition;

import static java.lang.Byte.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.Keep> {

    private Context context;
    private ArrayList<records> recordsArrayList;

    DB_Helpers dbHelpers;

    public adapter(Context context, ArrayList<records> recordsArrayList) {
        this.context = context;
        this.recordsArrayList = recordsArrayList;

        dbHelpers = new DB_Helpers(context);
    }

    @NonNull
    @Override // bağlama
    public Keep onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.logcardview, parent, false);
        return new Keep(view);
    }

    @Override // veri alıp aktarma
    public void onBindViewHolder(@NonNull Keep holder, int position) {

        // verileri pozisyonlarına göre alma
        records recs = recordsArrayList.get(position);

        String id = recs.getId();
        Bitmap image = recs.getImage(); // Bitmap türünde
        String name = recs.getName();
        String date = recs.getDate();

        if (image == null) {
            holder.imagee.setImageResource(R.drawable.baseline_account_circle_24);
        } else {
            holder.imagee.setImageBitmap(image); // Bitmap olarak ayarla
        }

        holder.namee.setText(name);
        holder.datee.setText(date);

        holder.delete.setTag(id);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setMessage("This data will be deleted permanently.");

                // Pozitif buton (örneğin, "Tamam" butonu)
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Pozitif butona tıklandığında yapılacak işlemler buraya yazılır
                        try {
                            // holder.delete butonunun etiketini (tag) kullanarak veriyi sil
                            String dataId = (String) holder.delete.getTag();
                            dbHelpers.deleteData(dataId); // Veriyi silmek için kullanılacak olan id değerini metoda geçir

                            // Veriyi listeden kaldır
                            recordsArrayList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());

                            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Negatif buton (örneğin, "İptal" butonu)
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Negatif butona tıklandığında yapılacak işlemler buraya yazılır
                        dialog.cancel(); // AlertDialog'u iptal et
                    }
                });

                // AlertDialog'u göster
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    @Override // satır sayısı
    public int getItemCount() {
        return recordsArrayList.size();
    }

    // kontroller
    public class Keep extends RecyclerView.ViewHolder {

        ImageView imagee;
        TextView namee, datee;

        ImageButton delete;

        public Keep(@NonNull View itemView) {
            super(itemView);

            imagee = itemView.findViewById(R.id.userimage);
            namee = itemView.findViewById(R.id.nameId);
            datee = itemView.findViewById(R.id.dateId);
            delete = itemView.findViewById(R.id.deletebuton);
        }
    }
}

