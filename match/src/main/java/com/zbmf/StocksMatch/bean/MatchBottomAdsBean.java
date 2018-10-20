package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/5/16.
 */

public class MatchBottomAdsBean extends Erro implements Serializable {
    private String status;
    private List<Adverts> match_invite_advert;
    private List<Adverts> match_buttom_advert;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Adverts> getMatch_invite_advert() {
        return match_invite_advert;
    }

    public void setMatch_invite_advert(List<Adverts> match_invite_advert) {
        this.match_invite_advert = match_invite_advert;
    }

    public List<Adverts> getMatch_buttom_advert() {
        return match_buttom_advert;
    }

    public void setMatch_buttom_advert(List<Adverts> match_buttom_advert) {
        this.match_buttom_advert = match_buttom_advert;
    }
}
