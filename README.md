# ModelView View Model with LiveData, RxJava Using Retrofit Network Call
> How You can use mvvm for RxJava LiveData and retofit its a piece of cake follow these steps hope this will help you. 

![](header.png)

## Dependency

Android Studio:build.gradle(module app)

```sh
    implementation "io.reactivex.rxjava2:rxjava:2.2.7"
    implementation "io.reactivex.rxjava2:rxandroid:2.1.1"
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    implementation "com.jakewharton.rxbinding3:rxbinding:3.0.0-alpha2"

    implementation "com.squareup.retrofit2:retrofit:2.6.2"
    implementation "com.squareup.retrofit2:converter-gson:2.4.0"
    implementation 'com.android.support:recyclerview-v7:29.0.0'
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    
    
    // Hilt
    implementation "com.google.dagger:hilt-android:2.28-alpha"
    annotationProcessor 'com.google.dagger:hilt-android-compiler:2.28-alpha'
    implementation 'androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02'
    annotationProcessor 'androidx.hilt:hilt-compiler:1.0.0-alpha02'
```

Android Studio:build.gradle(Project):

```sh
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.28-alpha'
    }
}
allprojects {
    repositories {
        google()
        jcenter()
       
    }
}

```

## Usage example

We Need 
1. Connection ( For API Interface Estrablishment)
2. API Interface ( For Server-Client Networking)
3. Models (Data Structure that will show which type of data should repository product and structure )
4. UI  
    -> Viewmodel (From Where User Will See the Data)
    -> view (From Where User Will See the Data)
    
## Development - 1st Connection 

For Any Network We need Connection : Here You can make Connection .  

  ```sh
public class ClientAPI {

    private static ClientAPI clientAPI;
    private APISettings apiSettings;

    public ClientAPI() {
            String base_url = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiSettings = retrofit.create(APISettings.class);
    }

    public static ClientAPI getClientAPI() {
        if (clientAPI == null){
            clientAPI= new ClientAPI();
        }
        return clientAPI;
    }

    public Single<DailyForecasts> getTemps(){
        return apiSettings.getTemp();
    }
}
```



## Development - 2nd API Interface 

We Need API Interface to END to END Point Connection and Method 

```sh

public interface APISettings {

    @GET("2606235?apikey=gJAkBa36Nt1cYt5LesFNER34ekTp5bUR")
    Single<DailyForecasts> getTemp();
}


```


## Development - 3rd Model  

We Need To Define What type of Model Should ViewModel Follow to Produce data  

```sh

public class DailyForecasts {

    @SerializedName("DailyForecasts")
    private List<Temperature> temperatureList = null;

    public DailyForecasts() { }

    public DailyForecasts(List<Temperature> temperatureList) {
        this.temperatureList = temperatureList;
    }

    public List<Temperature> getTemperatureList() {
        return temperatureList;
    }

    public void setTemperatureList(List<Temperature> temperatureList) {
        this.temperatureList = temperatureList;
    }
}

/****************************/

public class Temperature {

    @SerializedName("Date")
    private String Date;
    @SerializedName("EpochDate")
    private String EpochDate;
    @SerializedName("MobileLink")
    private String MobileLink;
    @SerializedName("Link")
    private String Link;

    public Temperature() { }

    public Temperature(String date, String epochDate, String mobileLink, String link) {
        Date = date;
        EpochDate = epochDate;
        MobileLink = mobileLink;
        Link = link;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEpochDate() {
        return EpochDate;
    }

    public void setEpochDate(String epochDate) {
        EpochDate = epochDate;
    }

    public String getMobileLink() {
        return MobileLink;
    }

    public void setMobileLink(String mobileLink) {
        MobileLink = mobileLink;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}


```


## Development - 4th UI viewModel

How viewModel Will Product the data From The Server and How it will Supply to the View

```sh

public class PostViewModel extends ViewModel {
    public    MutableLiveData<List<Temperature>> liveData= new MutableLiveData<>() ;
    CompositeDisposable disposable= new CompositeDisposable();

    public LiveData<List<Temperature>> getLiveData(){
        if (liveData==null)
            liveData = new MutableLiveData<>();
        return liveData;
    }
    public void getTempS(){

        Single<DailyForecasts> dailyForecastsSingle = ClientAPI.getClientAPI().getTemps()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        dailyForecastsSingle.subscribe(new SingleObserver<DailyForecasts>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull DailyForecasts dailyForecasts) {
                    liveData.setValue(dailyForecasts.getTemperatureList());
                    Log.e("Temps"," "+dailyForecasts.getTemperatureList().get(1).getDate());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("Temps",e.getMessage());
            }
        });

    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disposable.clear();
    }
}



```


## Development - 5th User End 

How Will Show the Data , UI Update and show 

```sh
public class MainActivity extends AppCompatActivity {

    PostViewModel postViewModel;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        postViewModel.getTempS();

        recyclerView = findViewById(R.id.r1);
        postAdapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        postViewModel.getLiveData().observe(this, new Observer<List<Temperature>>() {
            @Override
            public void onChanged(List<Temperature> temperatures) {
                postAdapter.setList(temperatures);
            }
        });
    }
}



```




## Meta

Ayoub GHOUDAN – [@ayoub_ghoudan] – ayoubghoudanos@gmail.com



