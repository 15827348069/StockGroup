package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class MatchDescBean extends Erro implements Serializable{
    private String status;
    private Result result;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{
        private int match_id;
        private String match_name;
        private int init_money;
        private int mpay;
        private String desc;
        private String rule;
        private String award_remark;
        private String award_detail;
        private String start_apply;
        private String end_apply;
        private String start_at;
        private String end_at;
        private String apply_require_field;
        private InviteMethod invite_method;
        private int match_type;
        private String template;
        private String match_status;
        private int canusecard;
        private RequiredField required_field;
        private int players;
        private int invite_type;
        private InviteMethod1 invite_method1;
        private int is_player;

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
        }

        public String getMatch_name() {
            return match_name;
        }

        public void setMatch_name(String match_name) {
            this.match_name = match_name;
        }

        public int getInit_money() {
            return init_money;
        }

        public void setInit_money(int init_money) {
            this.init_money = init_money;
        }

        public int getMpay() {
            return mpay;
        }

        public void setMpay(int mpay) {
            this.mpay = mpay;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getAward_remark() {
            return award_remark;
        }

        public void setAward_remark(String award_remark) {
            this.award_remark = award_remark;
        }

        public String getAward_detail() {
            return award_detail;
        }

        public void setAward_detail(String award_detail) {
            this.award_detail = award_detail;
        }

        public String getStart_apply() {
            return start_apply;
        }

        public void setStart_apply(String start_apply) {
            this.start_apply = start_apply;
        }

        public String getEnd_apply() {
            return end_apply;
        }

        public void setEnd_apply(String end_apply) {
            this.end_apply = end_apply;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public String getApply_require_field() {
            return apply_require_field;
        }

        public void setApply_require_field(String apply_require_field) {
            this.apply_require_field = apply_require_field;
        }

        public InviteMethod getInvite_method() {
            return invite_method;
        }

        public void setInvite_method(InviteMethod invite_method) {
            this.invite_method = invite_method;
        }

        public int getMatch_type() {
            return match_type;
        }

        public void setMatch_type(int match_type) {
            this.match_type = match_type;
        }

        public int getPlayers() {
            return players;
        }

        public void setPlayers(int players) {
            this.players = players;
        }

        public int getInvite_type() {
            return invite_type;
        }

        public void setInvite_type(int invite_type) {
            this.invite_type = invite_type;
        }

        public InviteMethod1 getInvite_method1() {
            return invite_method1;
        }

        public void setInvite_method1(InviteMethod1 invite_method1) {
            this.invite_method1 = invite_method1;
        }

        public boolean getIs_player() {
            return is_player==1;
        }

        public void setIs_player(int is_player) {
            this.is_player = is_player;
        }

        public String getMatch_status() {
            return match_status;
        }

        public void setMatch_status(String match_status) {
            this.match_status = match_status;
        }

        public int getCanusecard() {
            return canusecard;
        }

        public void setCanusecard(int canusecard) {
            this.canusecard = canusecard;
        }

        public RequiredField getRequired_field() {
            return required_field;
        }

        public void setRequired_field(RequiredField required_field) {
            this.required_field = required_field;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public class RequiredField implements Serializable{
            private int username;
            private int email;
            private int school;
            private int student_id;

            public int getUsername() {
                return username;
            }

            public void setUsername(int username) {
                this.username = username;
            }

            public int getEmail() {
                return email;
            }

            public void setEmail(int email) {
                this.email = email;
            }

            public int getSchool() {
                return school;
            }

            public void setSchool(int school) {
                this.school = school;
            }

            public int getStudent_id() {
                return student_id;
            }

            public void setStudent_id(int student_id) {
                this.student_id = student_id;
            }
        }

        public class InviteMethod implements Serializable{
            private InviteCode invite_code;

            public InviteCode getInvite_code() {
                return invite_code;
            }

            public void setInvite_code(InviteCode invite_code) {
                this.invite_code = invite_code;
            }

            public class InviteCode implements Serializable{
                private String method;
                private String code;

                public String getMethod() {
                    return method;
                }

                public void setMethod(String method) {
                    this.method = method;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }
            }
        }

        public class InviteMethod1 implements Serializable{
            private String invite_code;
            private String question;
            private String answer;

            public String getInvite_code() {
                return invite_code;
            }

            public void setInvite_code(String invite_code) {
                this.invite_code = invite_code;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }
        }
    }
}
