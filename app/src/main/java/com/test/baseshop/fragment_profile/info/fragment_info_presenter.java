package com.test.baseshop.fragment_profile.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.test.baseshop.R;

import java.util.HashMap;
import java.util.Objects;

public class fragment_info_presenter implements Interfaces.Presenter {

    private Interfaces.View view;
    private Interfaces.Model model;
    private int USER_ID;


    fragment_info_presenter(fragment_info view){
        this.view = view;
        this.model = new fragment_info_model(this);
        SharedPreferences sh = Objects.requireNonNull(view.getContext()).getSharedPreferences("AUTH_PREF",Context.MODE_PRIVATE);
        USER_ID = sh.getInt("USER_ID",0);
    }


    @Override
    public void onFocusChanged(View v, boolean hasFocus, View parentView) {
        int VISIBILITY;
        int id_of_commit_button = -1;
        if(hasFocus){
            VISIBILITY = View.VISIBLE;
        }else{
            VISIBILITY = View.INVISIBLE;
        }

        switch (v.getId()){
            case R.id.fragment_profile_fragment_info_email:
                id_of_commit_button = R.id.fragment_profile_fragment_info_email_commit;
                break;
            case R.id.fragment_profile_fragment_info_phone:
                id_of_commit_button = R.id.fragment_profile_fragment_info_phone_commit;
                break;
            case R.id.fragment_profile_fragment_info_name:
                id_of_commit_button = R.id.fragment_profile_fragment_info_name_commit;
                break;
        }
        Objects.requireNonNull(parentView).findViewById(id_of_commit_button).setVisibility(VISIBILITY);
    }

    @Override
    public void onCommitClick(android.view.View v, android.view.View parentView) {
        String input_text = null;
        String input_type_of_data = null;
        EditText row_view = null;
        switch (v.getId()){
            case R.id.fragment_profile_fragment_info_name_commit:
                row_view =  parentView.findViewById(R.id.fragment_profile_fragment_info_name);
                input_text = row_view.getText().toString();
                input_type_of_data = "first_name";
                break;
            case R.id.fragment_profile_fragment_info_phone_commit:
                row_view =  parentView.findViewById(R.id.fragment_profile_fragment_info_phone);
                input_text = row_view.getText().toString();
                input_type_of_data = "phone";
                break;
            case R.id.fragment_profile_fragment_info_email_commit:
                row_view =  parentView.findViewById(R.id.fragment_profile_fragment_info_email);
                input_text = row_view.getText().toString();
                input_type_of_data = "email";
                break;
        }
        String result_of_check = checkInputForCorrent(input_text);

        if(result_of_check == null) {
            result_of_check = model.sendNewInfoAboutUser(input_type_of_data,input_text,USER_ID);
            if(result_of_check == null){
                  view.hideKeyboardAndClearFocus();
            }else view.showError(result_of_check);
        }else view.showError(result_of_check);

    }

    @Override
    public void getUserInfo() {
        model.getUserInfo(USER_ID);
    }

    @Override
    public void setImageBySexOfUser(Context context, ImageView user_icon) {
        SharedPreferences sh = context.getSharedPreferences("AUTH_PREF",Context.MODE_PRIVATE);
        int sex_of_user = sh.getInt("USER_SEX",1);
        if(sex_of_user == 0){user_icon.setImageDrawable(context.getDrawable(R.drawable.fragment_profile_fragment_info_icon_woman));}
        else{user_icon.setImageDrawable(context.getDrawable(R.drawable.fragment_profile_fragment_info_icon_man));}
    }

    @Override
    public void clearPreferences(Context context) {
        SharedPreferences sh = context.getSharedPreferences("AUTH_PREF",Context.MODE_PRIVATE);
        sh.edit().clear().apply();
    }

    @Override
    public void setUserInfoFromModel(HashMap<String, String> userInfoFromModel) {
        view.hideProgressBar();
        view.setFirstName(userInfoFromModel.get("first_name"));
        view.setEmail(userInfoFromModel.get("email"));
        view.setPhone(userInfoFromModel.get("phone"));
    }


    private String checkInputForCorrent(String text){
        String result = null;
        if(text == null) result = "Что-то пошло не так(";
        else if(text.length() == 0) result = "Поле не заполнено";
        //TODO:ADD CHECK
        return result;
    }

}
