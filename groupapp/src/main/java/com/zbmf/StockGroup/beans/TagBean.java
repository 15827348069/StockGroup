package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2017/4/15.
 *
 */

public class TagBean implements Serializable{
    public TagBean() {
    }

    public TagBean(String tag_key, String tag_name, List<ChildrenTag>data) {
        this.tag_key = tag_key;
        this.tag_name = tag_name;
        this.data=data;
    }

    /**
     * tag_id : 55
     * tag_name : 准时
     */


    private String tag_key;
    private String tag_name;
    private List<ChildrenTag>data;
    private Map<String ,String>map;

    @Override
    public String toString() {
        return "TagBean{" +
                "tag_key='" + tag_key + '\'' +
                ", tag_name='" + tag_name + '\'' +
                ", data=" + data +
                ", map=" + map +
                '}';
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public List<ChildrenTag> getData() {
        return data;
    }

    public void setData(List<ChildrenTag> data) {
        this.data = data;
    }

    public String getTag_key() {
        return tag_key;
    }

    public void setTag_key(String tag_key) {
        this.tag_key = tag_key;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
    public static class ChildrenTag{
        private String name;
        private String id;
        private int is_hot;
        private boolean isSelect;


        public ChildrenTag(String name, String id, int is_hot) {
            this.name = name;
            this.id = id;
            this.is_hot = is_hot;
        }

        @Override
        public String toString() {
            return "ChildrenTag{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", is_hot=" + is_hot +
                    ", isSelect=" + isSelect +
                    '}';
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean getIs_hot() {
            return is_hot==1;
        }

        public void setIs_hot(int is_hot) {
            this.is_hot = is_hot;
        }
    }
}
