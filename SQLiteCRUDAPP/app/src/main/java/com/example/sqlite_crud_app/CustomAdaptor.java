package com.example.sqlite_crud_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.ViewHolder> {

    Activity activity;
    JSONArray jsonArray;

    MyDBHandler myDBHandler;

    DatePickerDialog dp;
    final Calendar calendar = Calendar.getInstance();

    public CustomAdaptor(Activity activity, JSONArray jsonArray) {
        this.activity = activity;
        this.jsonArray = jsonArray;
    }

    public void updateArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_row, parent, false);

        myDBHandler = new MyDBHandler(view.getContext());

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject object = jsonArray.getJSONObject(position);
            holder.tvSID.setText(object.getString("sid").toString());
            holder.tvSName.setText(object.getString("s_name").toString());
            holder.tvDOB.setText(object.getString("s_dob").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject object = jsonArray.getJSONObject(holder.getAdapterPosition());
                    int sid = Integer.parseInt(object.getString("sid").toString());
                    String s_name = object.getString("s_name").toString();
                    String s_dob = object.getString("s_dob").toString();

                    Dialog dialog = new Dialog(activity);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.setContentView(R.layout.custom_update_row);

                    dialog.show();

                    EditText etUpSName = dialog.findViewById(R.id.etUpSName);
                    EditText etUpDOB = dialog.findViewById(R.id.etUpDOB);
                    Button btnUpSubmit = dialog.findViewById(R.id.btnUpSubmit);

                    etUpSName.setText(s_name);
                    etUpDOB.setText(s_dob);

                    etUpDOB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int Day = calendar.get(calendar.DAY_OF_MONTH);
                            int Month = calendar.get(calendar.MONTH);
                            int Year = calendar.get(calendar.YEAR);
                            dp = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    etUpDOB.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                }
                            }, Day, Month, Year);
                            dp.show();
                        }
                    });

                    btnUpSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int position = holder.getAdapterPosition();

                            if (myDBHandler.Update(sid, etUpSName.getText().toString(), etUpDOB.getText().toString()) == 1) {
                                Toast.makeText(activity.getApplicationContext(), "Record Updated Successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "Something wrong.", Toast.LENGTH_SHORT).show();
                            }

                            etUpSName.setText("");
                            etUpDOB.setText("");
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();

                try {
                    JSONObject object = jsonArray.getJSONObject(position);
                    int sid = Integer.parseInt(object.getString("sid").toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure to delete?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (myDBHandler.Delete(sid) == 1) {
                                Toast.makeText(activity, "Record Deleted Successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "Something wrong.", Toast.LENGTH_SHORT).show();
                            }
                            jsonArray.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, jsonArray.length());
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSID, tvSName, tvDOB;
        Button btnUpdate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSID = itemView.findViewById(R.id.tvSID);
            tvSName = itemView.findViewById(R.id.tvSName);
            tvDOB = itemView.findViewById(R.id.tvDOB);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
