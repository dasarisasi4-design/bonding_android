package com.bondings.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(17, 11, 31)
    private val panel = Color.rgb(34, 23, 53)
    private val pink = Color.rgb(239, 72, 153)
    private val purple = Color.rgb(151, 80, 235)
    private val white = Color.WHITE
    private val muted = Color.rgb(190, 178, 207)
    private lateinit var content: LinearLayout
    private var activeTab = "Discover"

    data class Person(val name: String, val age: Int, val distance: String, val initials: String, val color: Int, val online: Boolean)
    private val people = listOf(
        Person("Aarav",25,"2 km","A",0xFF8E54E9.toInt(),true),
        Person("Rohan",27,"3 km","R",0xFFE94E9B.toInt(),true),
        Person("Kiran",24,"4 km","K",0xFF4776E6.toInt(),false),
        Person("Aditya",26,"5 km","A",0xFFDA6A45.toInt(),true),
        Person("Vikram",29,"6 km","V",0xFF269B91.toInt(),false),
        Person("Arjun",23,"7 km","A",0xFF9B59B6.toInt(),true),
        Person("Rahul",28,"8 km","R",0xFFCA5B78.toInt(),true),
        Person("Sahil",25,"9 km","S",0xFF4C78A8.toInt(),false),
        Person("Manu",27,"10 km","M",0xFFB7791F.toInt(),true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(bg) }
        val header = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(18),dp(18),dp(18),dp(12)) }
        header.addView(TextView(this).apply {
            text = "BONDINGS"; textSize = 23f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); letterSpacing = .08f
        }, LinearLayout.LayoutParams(0,dp(42),1f))
        header.addView(button("⚙ Filters",panel,white) { showFilters() })
        root.addView(header)
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(14),dp(4),dp(14),dp(8)) }
        root.addView(content,LinearLayout.LayoutParams(-1,0,1f))
        val nav = LinearLayout(this).apply { gravity = Gravity.CENTER; setPadding(dp(4),dp(8),dp(4),dp(8)); background = rounded(panel,18) }
        listOf("Discover","Likes","Chat","Profile").forEach { tab ->
            nav.addView(TextView(this).apply {
                text = (when(tab) { "Discover" -> "⌕"; "Likes" -> "♡"; "Chat" -> "▢"; else -> "●" }) + "\n" + tab
                gravity = Gravity.CENTER; textSize = 11f; setTextColor(if(activeTab==tab) pink else muted); setPadding(dp(4),dp(5),dp(4),dp(5))
                setOnClickListener { activeTab=tab; render() }
            },LinearLayout.LayoutParams(0,dp(52),1f))
        }
        val navWrap = FrameLayout(this).apply { setPadding(dp(12),dp(4),dp(12),dp(8)); addView(nav,FrameLayout.LayoutParams(-1,-2)) }
        root.addView(navWrap)
        setContentView(root)
        render()
    }

    private fun render() {
        content.removeAllViews()
        when(activeTab) {
            "Discover" -> discover()
            "Likes" -> { content.addView(heading("Your Likes")); content.addView(body("People you like and connection requests will appear here when your account is connected.")); content.addView(button("Explore profiles",purple,white){activeTab="Discover";render()}) }
            "Chat" -> { content.addView(heading("Messages")); content.addView(body("Your conversations will appear here after connecting your BONDINGS account.")); content.addView(button("Discover people",pink,white){activeTab="Discover";render()}) }
            else -> { content.addView(heading("My Profile")); content.addView(body("Your BONDINGS profile\\n\\nAdd your photo, display name, bio, interests and preferences.")); content.addView(button("Edit profile",purple,white){toast("Profile editor is the next integration step")}); content.addView(button("Settings & safety",panel,white){toast("Settings and safety are next")}) }
        }
    }

    private fun discover() {
        content.addView(heading("Discover people"))
        content.addView(TextView(this).apply { text="Find your people, your way."; textSize=13f; setTextColor(muted); setPadding(0,dp(2),0,dp(10)) })
        val scroll=ScrollView(this)
        val grid=GridLayout(this).apply { columnCount=3; useDefaultMargins=false }
        people.forEach { p ->
            val card=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; background=rounded(panel,12); clipToOutline=true; setOnClickListener{profile(p)} }
            val photo=FrameLayout(this).apply { background=GradientDrawable(GradientDrawable.Orientation.TL_BR,intArrayOf(p.color,blend(p.color))).apply{cornerRadius=dp(9).toFloat()} }
            photo.addView(TextView(this).apply{text=p.initials;textSize=42f;gravity=Gravity.CENTER;typeface=Typeface.DEFAULT_BOLD;setTextColor(white)},FrameLayout.LayoutParams(-1,-1))
            if(p.online) photo.addView(View(this).apply{background=oval(0xFF35D07F.toInt())},FrameLayout.LayoutParams(dp(10),dp(10),Gravity.TOP or Gravity.END).apply{setMargins(0,dp(7),dp(7),0)})
            card.addView(photo,LinearLayout.LayoutParams(-1,dp(116)))
            val info=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(dp(7),dp(6),dp(4),dp(8))}
            info.addView(TextView(this).apply{text="${p.name}, ${p.age}";textSize=12f;typeface=Typeface.DEFAULT_BOLD;setTextColor(white);maxLines=1})
            info.addView(TextView(this).apply{text="● ${p.distance}";textSize=10f;setTextColor(muted);setPadding(0,dp(3),0,0)})
            card.addView(info)
            grid.addView(card,GridLayout.LayoutParams().apply{width=0;height=-2;columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);setMargins(dp(3),dp(3),dp(3),dp(7))})
        }
        scroll.addView(grid)
        content.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))
    }

    private fun profile(p:Person) {
        content.removeAllViews()
        content.addView(button("← Back",panel,white){render()})
        content.addView(TextView(this).apply{text=p.initials;textSize=70f;gravity=Gravity.CENTER;setTextColor(white);background=GradientDrawable(GradientDrawable.Orientation.TL_BR,intArrayOf(p.color,purple)).apply{cornerRadius=dp(20).toFloat()}},LinearLayout.LayoutParams(-1,dp(210)).apply{topMargin=dp(14);bottomMargin=dp(14)})
        content.addView(heading("${p.name}, ${p.age}"))
        content.addView(body("${p.distance} away • ${if(p.online) "Online now" else "Recently active"}\\n\\nWelcome to my BONDINGS profile."))
        content.addView(button("♡ Connect",pink,white){toast("Connect action will be linked to Supabase next")})
        content.addView(button("Message",purple,white){activeTab="Chat";render()})
    }

    private fun showFilters() {
        val box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(dp(20),dp(12),dp(20),dp(8))}
        box.addView(heading("Discover filters"))
        box.addView(body("Age, distance and online filters will be connected to live profiles in the next integration step."))
        val age=EditText(this).apply{hint="Age range (e.g. 21–35)";setHintTextColor(muted);setTextColor(white);inputType=2}
        box.addView(age)
        android.app.AlertDialog.Builder(this).setView(box).setPositiveButton("Apply"){d,_->d.dismiss()}.setNegativeButton("Close",null).show()
    }

    private fun heading(s:String)=TextView(this).apply{text=s;textSize=21f;typeface=Typeface.DEFAULT_BOLD;setTextColor(white);setPadding(0,dp(10),0,dp(8))}
    private fun body(s:String)=TextView(this).apply{text=s;textSize=14f;setTextColor(muted);setPadding(0,dp(8),0,dp(16))}
    private fun button(s:String,c:Int,fg:Int,action:()->Unit)=TextView(this).apply{text=s;gravity=Gravity.CENTER;textSize=14f;typeface=Typeface.DEFAULT_BOLD;setTextColor(fg);setPadding(dp(14),dp(12),dp(14),dp(12));background=rounded(c,12);setOnClickListener{action()}}
    private fun rounded(c:Int,r:Int)=GradientDrawable().apply{setColor(c);cornerRadius=dp(r).toFloat()}
    private fun oval(c:Int)=GradientDrawable().apply{shape=GradientDrawable.OVAL;setColor(c);setStroke(dp(1),bg)}
    private fun blend(c:Int):Int=Color.rgb((Color.red(c)*.55+Color.red(bg)*.45).toInt(),(Color.green(c)*.55+Color.green(bg)*.45).toInt(),(Color.blue(c)*.55+Color.blue(bg)*.45).toInt())
    private fun dp(v:Int)=(v*resources.displayMetrics.density).toInt()
    private fun toast(s:String)=Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
}
