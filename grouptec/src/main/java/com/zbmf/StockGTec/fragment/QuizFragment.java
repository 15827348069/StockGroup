package com.zbmf.StockGTec.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.Chat1Activity;
import com.zbmf.StockGTec.adapter.QuestionAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.Ask;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 提问Fragment
 */
public class QuizFragment extends Fragment {
    private static final String mParam1 = "groupId";
    private PullToRefreshListView listview;
    private List<Ask> list = new ArrayList<>();
    private int page, PAGES;
    private QuestionAdapter adapter;
    private Dialog mDialog = null;
    private String groupId;
    private boolean isFisrt = true;

    public QuizFragment() {

    }

    public static QuizFragment newInstance(String groupId) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(mParam1, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getString(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tief, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        adapter = new QuestionAdapter(getActivity(), list);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setAdapter(adapter);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                groupAsks(1);
                groupAllAsks();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                groupAsks(2);
            }

        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ask ask = (Ask) parent.getItemAtPosition(position);
                if (ask.getAnswerType() == QuestionAdapter.TYPE_UNANSWERED) {
                    mDialog = null;
                    mDialog = answerDIA(ask);
                    mDialog.show();
                }
            }
        });

        adapter.setListener(new QuestionAdapter.DelItemListener() {
            @Override
            public void delete(String askId) {
                delQ(askId);
            }
        });

        if (isFisrt)
//        groupAsks(1);
            groupAllAsks();
    }

    private void delQ(final String ask_id) {
        WebBase.deleteAsks(ask_id, new JSONHandler(true, getActivity(), "正在删除问答...") {
            @Override
            public void onSuccess(JSONObject obj) {

                groupAllAsks();
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void groupAllAsks() {
        WebBase.groupAllAsks(new JSONHandler(getActivity()) {
            @Override
            public void onSuccess(JSONObject obj) {
                isFisrt = false;
                listview.onRefreshComplete();
                Ask ask = JSONparse.groupAsks(obj);
                list.clear();
                Chat1Activity aty = (Chat1Activity) getActivity();
                if (ask.getUnanswered_asks() != null && ask.getUnanswered_asks().size() > 0){
                    list.addAll(ask.getUnanswered_asks());
                    aty.setShowQuestion();
                }else{
                    aty.setQuestionNumbeGone();
                }

                if (ask.getAnswered_asks() != null && ask.getAnswered_asks().size() > 0)
                    list.addAll(ask.getAnswered_asks());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                listview.onRefreshComplete();
            }
        });
    }

//    private void groupAsks(final int d) {
//        if(d == 2){
//            page++;
//        }else{
//            page = 1;
//        }
//
//        WebBase.groupAsks(page, Constants.PER_PAGE, new JSONHandler(getActivity()) {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                isFisrt = false;
//                listview.onRefreshComplete();
//                Ask ask = JSONparse.groupAsks(obj);
//                if (ask != null) {
//                    if(d ==1 )
//                        list.clear();
//                    list.addAll(ask.getList());
//
//                    PAGES = ask.pages;
//                    if (page == PAGES) {
////                        Toast.makeText(getContext(), "已加载全部数据", Toast.LENGTH_SHORT).show();
//                        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                    }else
//                        listview.setMode(PullToRefreshBase.Mode.BOTH);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//                listview.onRefreshComplete();
//            }
//        });
//    }

    private Dialog answerDIA(final Ask ask) {
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.answer_layout, null);
        final EditText ed_answer = (EditText) layout.findViewById(R.id.ed_answer);
        final TextView tv_q = (TextView) layout.findViewById(R.id.tv_q);
        final Button btn_answer = (Button) layout.findViewById(R.id.btn_answer);//公开回复
        final Button btn_panswer = (Button) layout.findViewById(R.id.btn_panswer);//私密回复
        btn_answer.setEnabled(false);
        btn_answer.setAlpha(0.5f);
        tv_q.setText(ask.getAsk_content());
        if (ask.getIs_private() == 1) {
            btn_answer.setVisibility(View.GONE);
        }
        btn_answer.setEnabled(false);
        btn_panswer.setEnabled(false);
        ed_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("") || charSequence.length() == 0) {
                    btn_answer.setEnabled(false);
                    btn_answer.setAlpha(0.7f);
                    btn_panswer.setEnabled(false);
                    btn_panswer.setAlpha(0.7f);
                } else {
                    btn_answer.setEnabled(true);
                    btn_answer.setAlpha(1.0f);
                    btn_panswer.setEnabled(true);
                    btn_panswer.setAlpha(1.0f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAnswer(ask.getAsk_id(), ed_answer.getText().toString().trim(), "0");
                dialog.dismiss();
            }
        });

        btn_panswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAnswer(ask.getAsk_id(), ed_answer.getText().toString().trim(), "1");dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) ed_answer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        InputMethodManager inManager = (InputMethodManager) ed_answer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 100);
            }
        });
        return dialog;
    }

    private void postAnswer(String ask_id, String content, String isPrivate) {
        WebBase.post(ask_id, content, groupId, isPrivate, new JSONHandler(true, getActivity(), "加载中...") {
            @Override
            public void onSuccess(JSONObject obj) {

                groupAllAsks();
                Toast.makeText(getActivity(), "回答成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
