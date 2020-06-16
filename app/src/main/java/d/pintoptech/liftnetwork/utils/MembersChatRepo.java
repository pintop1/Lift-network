package d.pintoptech.liftnetwork.utils;

public class MembersChatRepo {
    private String sort, id, email, phone, name, helped, address, city, state, country,
            unread, views, about, passport1, passport2, passport3, help_with, gender, category, occupation, last_msg, age;

    public MembersChatRepo(String sort, String id, String email, String phone, String name, String helped, String address, String city, String state, String country, String unread,
                       String views, String about, String passport1, String passport2, String passport3, String help_with, String gender, String category, String occupation,
                       String last_msg, String age) {
        this.sort = sort;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.helped = helped;
        this.address = address;
        this.city = city;
        this.country = country;
        this.state = state;
        this.unread = unread;
        this.views = views;
        this.about = about;
        this.passport1 = passport1;
        this.passport2 = passport2;
        this.passport3 = passport3;
        this.help_with = help_with;
        this.gender = gender;
        this.category = category;
        this.occupation = occupation;
        this.last_msg = last_msg;
        this.age = age;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelped() {
        return helped;
    }

    public void setHelped(String helped) {
        this.helped = helped;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public String getPassport1() {
        return passport1;
    }

    public void setPassport1(String passport1) {
        this.passport1 = passport1;
    }
    public String getPassport2() {
        return passport2;
    }

    public void setPassport2(String passport2) {
        this.passport2 = passport2;
    }

    public String getPassport3() {
        return passport3;
    }

    public void setPassport3(String passport3) {
        this.passport3 = passport3;
    }

    public String getHelp_with() {
        return help_with;
    }

    public void setHelp_with(String help_with) {
        this.help_with = help_with;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
