/*
package es.source.code.activity.scos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.TextHolder> {
    private Context mContext;
    private List<String> texts;
    public  ItemAdapter(Context context, List<String> textInfo){
        mContext = context;
        texts = textInfo;
    }

    @Override
    public TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {          //创建viewholder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment01, parent, false);            //获取视图

        return new TextHolder(view);
    }

    @Override
    public void onBindViewHolder(TextHolder holder, int position) {                 //绑定数据
        String mtext = texts.get(position);

        holder.bindText(mtext);
    }

    @Override
    public int getItemCount() {                             //获取项目数量
        return texts.size();
    }

    public static class TextHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public TextHolder(View itemView) {
            super(itemView);

           // mTextView = (TextView) itemView.findViewById(R.id.list_items);
        }
        public void bindText(String mText){
            mTextView.setText(mText);
        }
    }
}*/
