package fr.uphf.a3ddy.model.posts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Page {

    @SerializedName("size")
    private int size;

    @SerializedName("number")
    private int number;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("content")
    private List<Post> content;


    public Page() {
    }

    public Page(int size, int number, int totalPages, List<Post> content) {
        this.size = size;
        this.number = number;
        this.totalPages = totalPages;
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Post> getPostList() {
        return content;
    }

    public void setPostList(List<Post> content) {
        this.content = content;
    }
}
