#### 简介
这是基于kotlin和WanAndroid开放API的APP。

#### 功能截图
![截图1](https://gitee.com/zhao-fangchen/image/raw/master/WanAndroid/%E5%8A%9F%E8%83%BD%E6%88%AA%E5%9B%BE1.jpg)
![截图2](https://gitee.com/zhao-fangchen/image/raw/master/WanAndroid/%E5%8A%9F%E8%83%BD%E6%88%AA%E5%9B%BE2.jpg)
#### 目前实现功能
1. 首页页面 MVVM 
- 首页banner： ViewPager2 + RecyclerView.Adapter
- 首页文章展示  RecyclerView + Retrofit2 + WebView
- 基于首页RecyclerView中已有文章搜索 menu + SearchView
- 收藏功能 
2. 搜索页面
- 返回对文章标题的检索
- 搜索热词 使用[鸿洋 FlowLayout](https://github.com/hongyangAndroid/FlowLayout)
3. 项目
- 获取项目目录 ViewPage + TabLayout
- 返回相应目录下的文章
4. 个人页面
- 个人收藏
5. 登录
-自动登录 使用PersistCookieJar


#### 其它
The open API: https://www.wanandroid.com/







