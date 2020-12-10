package test.room.api.myapplication;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.Viewholder> {

    private List<Data> data;
    private Context context;
    public static  String url="https://5fd241298cee610016adf235.mockapi.io/api/maindata/data";

    public DataAdapter(List<Data> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.iteam_layout, parent, false);

        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.Viewholder holder, int position) {
        holder.id.setText(String.valueOf(data.get(position).getId()));
        holder.ten.setText(data.get(position).getTen());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiDelete.DeleteApi(MainActivity.url, String.valueOf(data.get(position).getId()),context);

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= Integer.valueOf(data.get(position).getId());
                String value= String.valueOf(data.get(position).getTen());
                editData(id,value);
            }
        });



    }

    public void editData(final int id,String value){

        EditText edt_name;
        Button btnSubmit;
        final Dialog dialog= new Dialog(context);

        dialog.setContentView(R.layout.dialog_edit);

        edt_name= dialog.findViewById(R.id.txtEditName);
        btnSubmit= dialog.findViewById(R.id.btnUpdate);
        btnSubmit.setText("update");

        edt_name.setText(value);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data= edt_name.getText().toString();
                Submit("PUT",data, dialog,id);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    private void Submit(String method, final String data, final Dialog dialog, final int id) {
        if(method == "PUT") {
            StringRequest request = new StringRequest(Request.Method.PUT, url+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));
                    params.put("ten", data);
                    return params;
                }
            };
            Volley.newRequestQueue(context).add(request);

        }
    }





    @Override
    public int getItemCount() {
        return data.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
         TextView id;
       TextView ten;

        Button edit,del;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            ten = itemView.findViewById(R.id.ten);

            edit = itemView.findViewById(R.id.edit);
            del = itemView.findViewById(R.id.xoa);
        }
    }
}
