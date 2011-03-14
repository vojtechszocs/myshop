package sk.myshop.app.server.domain.merchant;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

public class PictureCached implements Serializable {

    private static final long serialVersionUID = 7292701682583692907L;

    private Key key;
    private Set<String> keywords;
    private String name;
    private byte[] data;

    protected PictureCached() {
    }

    public PictureCached(Key key, Set<String> keywords, String name, byte[] data) {
        setKey(key);
        setKeywords(keywords);
        setName(name);
        setData(data);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Set<String> getKeywords() {
        return Collections.unmodifiableSet(keywords);
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = new HashSet<String>(keywords);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
