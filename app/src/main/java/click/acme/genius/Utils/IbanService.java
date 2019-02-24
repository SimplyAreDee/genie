package click.acme.genius.Utils;

import click.acme.genius.Models.Book;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IbanService {
    @GET("/book/{iban}")
    Observable<Book> getBookInfos(@Path("iban") String iban);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.isbndb.com/book/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}