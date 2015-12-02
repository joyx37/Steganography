package com.example.ajoy3.steganography;

/**
 * Created by Malabika on 11/28/2015.
 */
public class PairInformation {
    private int _id;
    private String _nameofpairer, _nickname, _sharedkey;
//    private String _latlngentries;

    public PairInformation(int id, String nameofpairer, String nickname, String sharedkey){
        _id = id;
        _nameofpairer = nameofpairer;
        _nickname = nickname;
        _sharedkey = sharedkey;
    }

    public int getId(){ return _id;}
    public String get_nameofpairer(){ return _nameofpairer;}
    public String get_nickname(){ return _nickname;}
    public String get_sharedkey(){ return _sharedkey;}

    public void set_id(int id){_id = id; }
    public void set_nameofpairer(String nameofpairer){_nameofpairer = nameofpairer; }
    public void set_nickname(String nickname){_nickname = nickname; }
    public void set_sharedkey(String sharedkey){_sharedkey = sharedkey; }
}
