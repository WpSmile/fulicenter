package cn.ucai.fulicenter.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public class PropertiesBean {

    private ColorBean colorBean;

    private AlbumsBean[] albumsBeen;

    public PropertiesBean() {
    }

    public ColorBean getColorBean() {
        return colorBean;
    }

    public void setColorBean(ColorBean colorBean) {
        this.colorBean = colorBean;
    }

    public AlbumsBean[] getAlbumsBeen() {
        return albumsBeen;
    }

    public void setAlbumsBeen(AlbumsBean[] albumsBeen) {
        this.albumsBeen = albumsBeen;
    }

    @Override
    public String toString() {
        return "PropertiesBean{" +
                "colorBean=" + colorBean +
                ", albumsBeen=" + Arrays.toString(albumsBeen) +
                '}';
    }
}
