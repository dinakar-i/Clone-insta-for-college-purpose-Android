package com.spoof.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Model.Message;
import com.spoof.Model.Msg_notify;
import com.spoof.lm.ChatActivity;
import com.spoof.lm.MessageActivity;
import com.spoof.lm.R;
import com.spoof.lm.StartActivity;
import com.spoof.lm.WebActivity;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messageList;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    public MessageAdapter(Context mContext, List<Message> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0)
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left ,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Message message=messageList.get(position);
          holder.message_right.setText(message.getMessage());
          readMessage(message.getReceiver(),firebaseUser.getUid(),message.getMessageId(),message.getStatus());
          if(message.getStatus().equals("seen"))
          {
              holder.message_track_img.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_up_24);
              holder.message_track_img.setColorFilter(Color.GREEN);
          }else if(message.getStatus().equals("received"))
          {
              holder.message_track_img.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_up_24);
              holder.message_track_img.setColorFilter(Color.WHITE);
          }
          else
          {
              holder.message_track_img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
              holder.message_track_img.setColorFilter(Color.WHITE);
          }
          showMessage(holder,message.getSender(),firebaseUser.getUid(),message.getMessage(),message.getTime(),message.getDate(),message.getMessageId(),message.getType());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView message_right;
        private ImageView message_track_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_right=itemView.findViewById(R.id.message_text);
            message_track_img=itemView.findViewById(R.id.message_track_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    private void readMessage(String receiverId,String myId,String messageId,String messageStatus)
    {
        databaseReference.child("Msg_notify").child(firebaseUser.getUid()).child(messageId).removeValue();
        if(!messageStatus.equals("seen") && receiverId.equals(myId))
        {
            databaseReference.child("Chats").child(messageId).child("status").setValue("seen");
        }
    }

    private void showMessage(ViewHolder holder,String senderId,String myId,String message,String time,String date,String messageId,String type)
    {
        if(type!=null)
        {
           if(type.equals("img"))
           {
               holder.message_right.setTextColor(Color.BLUE);
               holder.message_right.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(Intent.ACTION_VIEW);
                       intent.setData(Uri.parse(message));
                       mContext.startActivity(intent);
                   }
               });
           }else
           {
               holder.message_right.setTextColor(Color.WHITE);
           }
        }
        holder.message_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle(message);
                    builder.setMessage("Time: "+time+"\n"+"Date: "+date);
                    builder.setNeutralButton("Delete Chat", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           if(senderId.equals(myId))
                           {
                               databaseReference.child("Chats").child(messageId).child("status").setValue("deleted").addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful())
                                       {
                                           Toast.makeText(mContext, "Chat deleted🤗.", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                           }
                           else
                           {
                               Toast.makeText(mContext, "Don't have permission to delete this chat😆.", Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                return true;
            }
        });

    }
}
