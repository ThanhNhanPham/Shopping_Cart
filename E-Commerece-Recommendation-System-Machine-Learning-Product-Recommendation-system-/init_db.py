import pymysql
from sqlalchemy import create_engine, text

# Cấu hình MySQL
MYSQL_USER = 'root'
MYSQL_PASSWORD = '09102004phuc'  # Mật khẩu MySQL của bạn
MYSQL_HOST = 'localhost'
DATABASE_NAME = 'ecom'

# Bước 1: Tạo database nếu chưa tồn tại
print("📦 Bước 1: Kết nối MySQL và tạo database...")
try:
    # Kết nối không cần database name
    connection = pymysql.connect(
        host=MYSQL_HOST,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD
    )
    
    with connection.cursor() as cursor:
        # Tạo database nếu chưa có
        cursor.execute(f"CREATE DATABASE IF NOT EXISTS {DATABASE_NAME}")
        print(f"✅ Database '{DATABASE_NAME}' đã sẵn sàng!")
    
    connection.close()
    
except Exception as e:
    print(f"❌ Lỗi khi tạo database: {e}")
    print("\n💡 Vui lòng kiểm tra:")
    print("   1. MySQL server đang chạy")
    print("   2. Thông tin đăng nhập MySQL đúng")
    exit(1)

# Bước 2: Tạo các bảng
print("\n📋 Bước 2: Tạo các bảng...")
try:
    from app import app, db, Signup, Signin, UserPreferences
    
    with app.app_context():
        # Tạo tất cả các bảng
        db.create_all()
        print("✅ Đã tạo tất cả các bảng thành công!")
        print("\n📋 Các bảng đã tạo:")
        print("   - signup (thông tin đăng ký)")
        print("   - signin (thông tin đăng nhập)")
        print("   - user_preferences (sở thích người dùng)")
        print("\n🚀 Bạn có thể chạy ứng dụng với: python app.py")
        
except Exception as e:
    print(f"❌ Lỗi khi tạo bảng: {e}")
    print("\n💡 Lỗi có thể do:")
    print("   1. Các file CSV (clean_data.csv, trending_products.csv) không tồn tại")
    print("   2. Database connection string sai")
    print(f"\n📝 Chi tiết lỗi: {str(e)}")
    exit(1)