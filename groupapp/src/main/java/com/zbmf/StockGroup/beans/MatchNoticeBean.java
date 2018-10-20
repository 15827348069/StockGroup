package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/5/30.
 */

public class MatchNoticeBean {
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

    public static class Result implements Serializable {
        private int page;
        private int perpage;
        private int pages;
        private int total;
        private List<Announcements> announcements;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPerpage() {
            return perpage;
        }

        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Announcements> getAnnouncements() {
            return announcements;
        }

        public void setAnnouncements(List<Announcements> announcements) {
            this.announcements = announcements;
        }

        public class Announcements implements Serializable {
            private String topic_id;
            private String subject;
            private String content;
            private String posted_at;

            public String getTopic_id() {
                return topic_id;
            }

            public void setTopic_id(String topic_id) {
                this.topic_id = topic_id;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPosted_at() {
                return posted_at;
            }

            public void setPosted_at(String posted_at) {
                this.posted_at = posted_at;
            }
        }
    }
}
