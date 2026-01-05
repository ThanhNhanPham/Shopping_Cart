from flask import Flask, request, render_template, redirect, url_for, session
import pandas as pd
import random
import json
from flask_sqlalchemy import SQLAlchemy
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

app = Flask(__name__)

# load files===========================================================================================================
trending_products = pd.read_csv("trending_products.csv")
train_data = pd.read_csv("clean_data.csv")

# database configuration---------------------------------------
app.secret_key = "alskdjfwoeieiurlskdjfslkdjf"
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql://root:@localhost/ecom"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)


# Define your model class for the 'signup' table
class Signup(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(100), nullable=False)
    email = db.Column(db.String(100), nullable=False)
    password = db.Column(db.String(100), nullable=False)

# Define your model class for the 'signin' table
class Signin(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(100), nullable=False)
    password = db.Column(db.String(100), nullable=False)


# Define your model class for user preferences
class UserPreferences(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('signup.id'), nullable=False)
    preferred_categories = db.Column(db.Text, nullable=True)  # JSON string
    preferred_brands = db.Column(db.Text, nullable=True)      # JSON string
    min_rating = db.Column(db.Float, default=0.0)
    min_review_count = db.Column(db.Integer, default=0)
    preferred_keywords = db.Column(db.Text, nullable=True)    # JSON string
    created_at = db.Column(db.DateTime, default=db.func.current_timestamp())


# Recommendations functions============================================================================================
# Function to truncate product name
def truncate(text, length):
    if len(text) > length:
        return text[:length] + "..."
    else:
        return text


def content_based_recommendations(train_data, item_name, top_n=10):
    # Check if the item name exists in the training data
    if item_name not in train_data['Name'].values:
        print(f"Item '{item_name}' not found in the training data.")
        return pd.DataFrame()

    # Create a TF-IDF vectorizer for item descriptions
    tfidf_vectorizer = TfidfVectorizer(stop_words='english')

    # Apply TF-IDF vectorization to item descriptions
    tfidf_matrix_content = tfidf_vectorizer.fit_transform(train_data['Tags'])

    # Calculate cosine similarity between items based on descriptions
    cosine_similarities_content = cosine_similarity(tfidf_matrix_content, tfidf_matrix_content)

    # Find the index of the item
    item_index = train_data[train_data['Name'] == item_name].index[0]

    # Get the cosine similarity scores for the item
    similar_items = list(enumerate(cosine_similarities_content[item_index]))

    # Sort similar items by similarity score in descending order
    similar_items = sorted(similar_items, key=lambda x: x[1], reverse=True)

    # Get the top N most similar items (excluding the item itself)
    top_similar_items = similar_items[1:top_n+1]

    # Get the indices of the top similar items
    recommended_item_indices = [x[0] for x in top_similar_items]

    # Get the details of the top similar items
    recommended_items_details = train_data.iloc[recommended_item_indices][['Name', 'ReviewCount', 'Brand', 'ImageURL', 'Rating']]

    return recommended_items_details


def hybrid_personalized_recommendations(train_data, preferences, user_profile_keywords, top_n=20):
    """
    Kết hợp filtering + TF-IDF cho gợi ý cá nhân hóa
    
    Args:
        train_data: DataFrame chứa dữ liệu sản phẩm
        preferences: Dict chứa sở thích người dùng (category, brand, rating, etc.)
        user_profile_keywords: String mô tả sở thích người dùng (để tính TF-IDF)
        top_n: Số sản phẩm gợi ý
    """
    
    # BƯỚC 1: Lọc sản phẩm theo preferences (Rule-based Filtering)
    filtered_data = train_data.copy()
    
    # Lọc theo Category
    if preferences.get('categories') and len(preferences['categories']) > 0:
        category_filter = '|'.join(preferences['categories'])
        filtered_data = filtered_data[
            filtered_data['Category'].str.contains(category_filter, case=False, na=False)
        ]
    
    # Lọc theo Brand
    if preferences.get('brands') and len(preferences['brands']) > 0:
        brand_filter = '|'.join(preferences['brands'])
        filtered_data = filtered_data[
            filtered_data['Brand'].str.contains(brand_filter, case=False, na=False)
        ]
    
    # Lọc theo Rating
    if preferences.get('min_rating', 0) > 0:
        filtered_data = filtered_data[filtered_data['Rating'] >= preferences['min_rating']]
    
    # Lọc theo ReviewCount
    if preferences.get('min_review_count', 0) > 0:
        filtered_data = filtered_data[
            filtered_data['ReviewCount'] >= preferences['min_review_count']
        ]
    
    # Kiểm tra xem còn sản phẩm không
    if filtered_data.empty:
        print("Không có sản phẩm phù hợp với tiêu chí lọc")
        return pd.DataFrame()
    
    # BƯỚC 2: Áp dụng TF-IDF trên tập đã lọc
    # Tạo TF-IDF vectorizer
    tfidf_vectorizer = TfidfVectorizer(stop_words='english', max_features=500)
    
    # Fit trên Tags của sản phẩm đã lọc
    filtered_data_reset = filtered_data.reset_index(drop=True)
    tfidf_matrix = tfidf_vectorizer.fit_transform(filtered_data_reset['Tags'].fillna(''))
    
    # Tạo vector cho user profile (nếu có keywords)
    if user_profile_keywords.strip():
        user_vector = tfidf_vectorizer.transform([user_profile_keywords])
        # Tính cosine similarity giữa user profile và các sản phẩm
        similarities = cosine_similarity(user_vector, tfidf_matrix)[0]
    else:
        # Nếu không có keywords, gán similarity = 0 cho tất cả
        similarities = np.zeros(len(filtered_data_reset))
    
    # Thêm similarity score vào DataFrame
    filtered_data_reset['similarity_score'] = similarities
    
    # BƯỚC 3: Kết hợp nhiều yếu tố để xếp hạng
    # Normalized rating (0-1)
    filtered_data_reset['norm_rating'] = filtered_data_reset['Rating'] / 5.0
    
    # Normalized review count (log scale)
    max_reviews = filtered_data_reset['ReviewCount'].max()
    if max_reviews > 0:
        filtered_data_reset['norm_reviews'] = np.log1p(filtered_data_reset['ReviewCount']) / np.log1p(max_reviews)
    else:
        filtered_data_reset['norm_reviews'] = 0
    
    # Tính điểm tổng hợp (có thể điều chỉnh trọng số)
    filtered_data_reset['final_score'] = (
        0.5 * filtered_data_reset['similarity_score'] +  # 50% từ TF-IDF similarity
        0.3 * filtered_data_reset['norm_rating'] +       # 30% từ rating
        0.2 * filtered_data_reset['norm_reviews']        # 20% từ popularity
    )
    
    # Sắp xếp theo điểm tổng hợp
    filtered_data_reset = filtered_data_reset.sort_values('final_score', ascending=False)
    
    # Trả về top N sản phẩm
    return filtered_data_reset.head(top_n)[
        ['Name', 'ReviewCount', 'Brand', 'ImageURL', 'Rating', 'Category', 
         'similarity_score', 'final_score']
    ]

# routes===============================================================================
# List of predefined image URLs
random_image_urls = [
    "static/img/img_1.png",
    "static/img/img_2.png",
    "static/img/img_3.png",
    "static/img/img_4.png",
    "static/img/img_5.png",
    "static/img/img_6.png",
    "static/img/img_7.png",
    "static/img/img_8.png",
]


@app.route("/")
def index():
    # Create a list of random image URLs for each product
    random_product_image_urls = [random.choice(random_image_urls) for _ in range(len(trending_products))]
    price = [40, 50, 60, 70, 100, 122, 106, 50, 30, 50]
    return render_template('index.html',trending_products=trending_products.head(8),truncate = truncate,
                           random_product_image_urls=random_product_image_urls,
                           random_price = random.choice(price))

@app.route("/main")
def main():
    return render_template('main.html')

# routes
@app.route("/index")
def indexredirect():
    # Create a list of random image URLs for each product
    random_product_image_urls = [random.choice(random_image_urls) for _ in range(len(trending_products))]
    price = [40, 50, 60, 70, 100, 122, 106, 50, 30, 50]
    return render_template('index.html', trending_products=trending_products.head(8), truncate=truncate,
                           random_product_image_urls=random_product_image_urls,
                           random_price=random.choice(price))

@app.route("/signup", methods=['POST','GET'])
def signup():
    if request.method=='POST':
        username = request.form['username']
        email = request.form['email']
        password = request.form['password']

        new_signup = Signup(username=username, email=email, password=password)
        db.session.add(new_signup)
        db.session.commit()

        # Create a list of random image URLs for each product
        random_product_image_urls = [random.choice(random_image_urls) for _ in range(len(trending_products))]
        price = [40, 50, 60, 70, 100, 122, 106, 50, 30, 50]
        return render_template('index.html', trending_products=trending_products.head(8), truncate=truncate,
                               random_product_image_urls=random_product_image_urls, random_price=random.choice(price),
                               signup_message='User signed up successfully!'
                               )
    return None


# Route for signin page
@app.route('/signin', methods=['POST', 'GET'])
def signin():
    if request.method == 'POST':
        username = request.form['signinUsername']
        password = request.form['signinPassword']
        
        # Kiểm tra user có tồn tại không
        user = Signup.query.filter_by(username=username).first()
        
        if user and user.password == password:
            # Đăng nhập thành công, lưu user_id vào session
            session['user_id'] = user.id
            session['username'] = user.username
            # Redirect đến form preferences
            return redirect(url_for('show_preferences'))
        else:
            # Đăng nhập thất bại
            random_product_image_urls = [random.choice(random_image_urls) for _ in range(len(trending_products))]
            price = [40, 50, 60, 70, 100, 122, 106, 50, 30, 50]
            return render_template('index.html', 
                                 trending_products=trending_products.head(8), 
                                 truncate=truncate,
                                 random_product_image_urls=random_product_image_urls, 
                                 random_price=random.choice(price),
                                 error_message='Sai tên đăng nhập hoặc mật khẩu!'
                                 )
    return None


@app.route("/preferences")
def show_preferences():
    """Hiển thị form thu thập sở thích người dùng"""
    if 'username' not in session:
        return redirect(url_for('index'))
    return render_template('preference_form.html', username=session.get('username'))


@app.route("/save_preferences", methods=['POST'])
def save_preferences():
    """Xử lý form preferences và hiển thị sản phẩm gợi ý"""
    if 'user_id' not in session:
        return redirect(url_for('index'))
    
    # Lấy thông tin từ form
    categories = request.form.getlist('categories')
    brands = request.form.get('brands', '').split(',')
    brands = [b.strip() for b in brands if b.strip()]
    
    min_rating = float(request.form.get('min_rating', 0))
    min_reviews = int(request.form.get('min_reviews', 0))
    keywords = request.form.get('keywords', '').split(',')
    keywords = [k.strip() for k in keywords if k.strip()]
    
    # Tạo dict preferences
    preferences = {
        'categories': categories,
        'brands': brands,
        'min_rating': min_rating,
        'min_review_count': min_reviews,
    }
    
    # Lưu preferences vào database
    user_pref = UserPreferences.query.filter_by(user_id=session['user_id']).first()
    if user_pref:
        # Cập nhật preferences hiện có
        user_pref.preferred_categories = json.dumps(categories)
        user_pref.preferred_brands = json.dumps(brands)
        user_pref.min_rating = min_rating
        user_pref.min_review_count = min_reviews
        user_pref.preferred_keywords = json.dumps(keywords)
    else:
        # Tạo mới preferences
        user_pref = UserPreferences(
            user_id=session['user_id'],
            preferred_categories=json.dumps(categories),
            preferred_brands=json.dumps(brands),
            min_rating=min_rating,
            min_review_count=min_reviews,
            preferred_keywords=json.dumps(keywords)
        )
        db.session.add(user_pref)
    
    db.session.commit()
    
    # Tạo user profile keywords từ preferences
    user_profile_keywords = ' '.join(categories + brands + keywords)
    
    # GỌI HÀM HYBRID (kết hợp filtering + TF-IDF)
    recommended_products = hybrid_personalized_recommendations(
        train_data, 
        preferences, 
        user_profile_keywords, 
        top_n=20
    )
    
    if recommended_products.empty:
        message = "Không tìm thấy sản phẩm phù hợp với sở thích của bạn. Vui lòng thử lại!"
        return render_template('preference_form.html', 
                             message=message,
                             username=session.get('username'))
    
    # Hiển thị kết quả
    random_product_image_urls = [random.choice(random_image_urls) for _ in range(len(recommended_products))]
    price = [40, 50, 60, 70, 100, 122, 106, 50, 30, 50]
    
    return render_template('personalized_results.html', 
                         products=recommended_products,
                         truncate=truncate,
                         random_product_image_urls=random_product_image_urls,
                         random_price=random.choice(price),
                         username=session.get('username'))


@app.route("/recommendations", methods=['POST', 'GET'])
def recommendations():
    if request.method == 'POST':
        prod = request.form.get('prod')
        nbr = int(request.form.get('nbr'))
        content_based_rec = content_based_recommendations(train_data, prod, top_n=nbr)

        if content_based_rec.empty:
            message = "No recommendations available for this product."
            return render_template('main.html', message=message)
        else:
            # Create a list of random image URLs for each recommended product
            random_product_image_urls = [random.choice(random_image_urls) for _ in range(len(trending_products))]
            print(content_based_rec)
            print(random_product_image_urls)

            price = [40, 50, 60, 70, 100, 122, 106, 50, 30, 50]
            return render_template('main.html', content_based_rec=content_based_rec, truncate=truncate,
                                   random_product_image_urls=random_product_image_urls,
                                   random_price=random.choice(price))
    return None


if __name__=='__main__':
    app.run(debug=True)