package com.example.cylife;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

  private final Context context;
  private final List<Chat> chatList;
  private final int userId;
  private final String name; // Add userId field to be passed to ChatScreenActivity

  public ChatAdapter(Context context, List<Chat> chatList, int userId, String name) {
    this.context = context;
    this.chatList = chatList;
    this.userId = userId;
    this.name = name;
  }

  @NonNull
  @Override
  public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
    return new ChatViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
    Chat chat = chatList.get(position);
    holder.chatName.setText(chat.getChatName());
    holder.chatIcon.setImageResource(chat.getChatIcon());

    holder.itemView.setOnClickListener(
        v -> {
          Intent intent = new Intent(context, ChatScreenActivity.class);
          intent.putExtra("clubId", chat.getId());
          intent.putExtra("userID", userId);
          intent.putExtra("Name", name);
          context.startActivity(intent);
        });
  }

  @Override
  public int getItemCount() {
    return chatList.size();
  }

  public static class ChatViewHolder extends RecyclerView.ViewHolder {
    TextView chatName;
    ImageView chatIcon;

    public ChatViewHolder(@NonNull View itemView) {
      super(itemView);
      chatName = itemView.findViewById(R.id.textView_chat_name);
      chatIcon = itemView.findViewById(R.id.imageView_chat_icon);
    }
  }
}
