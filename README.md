#### 简介
这是基于kotlin和WanAndroid开放API的APP。


#### 目前实现功能
1. 首页页面
- 首页banner： ViewPager2 + RecyclerView.Adapter
- 首页文章展示  RecyclerView + Retrofit2 + WebView
- 基于首页RecyclerView中已有文章搜索 menu + SearchView
2. 搜索页面
- 返回对文章标题的检索
- 搜索热词 使用[鸿洋 FlowLayout](https://github.com/hongyangAndroid/FlowLayout)
3. 项目
- 获取项目目录 ViewPage + TabLayout
- 返回相应目录下的文章
4. 个人页面
- 实现UI
5. 登录
-自动登录 使用OKHTTP读取和返回cookies，使用SharedPreferences存储登录响应中返回的包含用户名和密码的cookies


#### 其它
The open API: https://www.wanandroid.com/







