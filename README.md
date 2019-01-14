# Tính Năng Chính 
- [x] Login/Sign up
- [x] Tạo một shop của chính mình.
- [ ] Chỉnh sửa thông tin của một shop/ chỉnh sửa thông tin của một sản phẩm
- Thông tin một shop:
  - [ ] Ảnh đại diện của shop
  - [ ] Tên shop 
  - [ ] Giờ đóng cửa/mở cửa
  - [ ] Số điện thoại
  - [ ] Địa điểm (location)
  - [ ] Danh sách các sản phẩm: tên, hình ảnh, giá

* [ ] Tìm kiếm shop, sản phẩm:
  * [ ] Tìm kiếm cơ bản: text
  * [ ] Tìm kiếm bằng hình ảnh

- [ ] Chọn một item vào danh sách yêu thích 
- [ ] Chỉ đường đến shop
- [ ] Chọn mua, là có thông báo đến chủ shop đó
- [ ] Chat giữa người mua và chủ shop (nếu muốn =]] )
- [ ] Shopping planner: lên kế hoạch đi shopping (Để sau)
# Các màn hình chính:
1. [x] Màn hình sign in / sign up
2. Màn hình trang chủ:
- Thanh search 
- Top các shop
- Top các sản phẩm
- Danh mục sản phẩm 
3. Profile: 
- [x] Avatar
- Hình ảnh (???)
- [ ] Danh sách sản phẩm yêu thích 
- [x] Danh sách shop của mình
4. [x] Màn hình tạo shop: gồm các thông tin cần thiết 
5. Màn hình chi tiết shop
6. Màn hình chi tiết sản phẩm 
7. Màn hình tất cả các sản phẩm (có filter)

# Database 
1. User
- uid
- name
2. Shop
- owner: uid
- shopName
- address: chuỗi
- location: lat lng
- open_hour
- close_hour
3. Item:
- iid
- shop: sid
- name:
- category:
- price:
- image:
- quantity:
- variation:
    + color 
    + size
