package com.nomargin.cynema.ui.fragment.home_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.data.repository.AuthenticationRepository
import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.data.repository.SharedPreferencesRepository
import com.nomargin.cynema.data.usecase.AppLocalDatabaseUseCase
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import com.nomargin.cynema.util.model.CarouselModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theMovieDatabaseApiUseCase: TheMovieDatabaseApiUseCase,
    private val appLocalDatabaseUseCase: AppLocalDatabaseUseCase,
    private val profileRepository: ProfileRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : ViewModel() {

    private val _genres: MutableLiveData<List<GenreModel>> = MutableLiveData()
    val genres: LiveData<List<GenreModel>> = _genres
    private val _movieModelToCarouselModel: MutableLiveData<List<CarouselModel>> = MutableLiveData()
    val movieModelToCarouselModel: LiveData<List<CarouselModel>> = _movieModelToCarouselModel
    private val _nowPlayingMovies: MutableLiveData<List<MovieModel>> = MutableLiveData()
    val nowPlayingMovies: LiveData<List<MovieModel>> = _nowPlayingMovies
    private val _topRatedMovies: MutableLiveData<List<MovieModel>> = MutableLiveData()
    val topRatedMovies: LiveData<List<MovieModel>> = _topRatedMovies
    private val _upcomingMovies: MutableLiveData<List<MovieModel>> = MutableLiveData()
    val upcomingMovies: LiveData<List<MovieModel>> = _upcomingMovies
    private val _userProfileData: MutableLiveData<UserProfileDataModel> = MutableLiveData()
    val userProfileData: LiveData<UserProfileDataModel> = _userProfileData

    fun getHomeScreenData() {
        getGenres()
        getPopularMovies()
        getNowPlayingMovies()
        getTopRatedMovies()
        getUpcomingMovies()
        getUserProfileData()
    }

    private fun getGenres() = viewModelScope.launch {
        appLocalDatabaseUseCase.insertGenres(theMovieDatabaseApiUseCase.getMovieGenres().genreList)
        _genres.value = appLocalDatabaseUseCase.selectAllGenres()
    }

    private fun getMovieModelToCarouselModel(movieList: List<MovieModel>) = viewModelScope.launch {
        val movieCarouselModel: MutableList<CarouselModel> = mutableListOf()
        for (movie in movieList) {
            val movieGenres: MutableList<GenreModel> = mutableListOf()
            for (movieGenre in movie.genreIds) {
                val genre = appLocalDatabaseUseCase.selectGenreById(movieGenre)
                movieGenres.add(genre)
            }
            movieCarouselModel.add(
                CarouselModel(
                    id = movie.movieId,
                    title = movie.title,
                    backgroundPath = movie.backdropPath,
                    posterPath = movie.posterPath,
                    genres = movieGenres
                )
            )
        }
        _movieModelToCarouselModel.value = movieCarouselModel
    }

    private fun getPopularMovies() = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            getMovieModelToCarouselModel(
                theMovieDatabaseApiUseCase.getPopularMovies().results
            )
        }
    }

    private fun getNowPlayingMovies() = viewModelScope.launch {
        _nowPlayingMovies.value = theMovieDatabaseApiUseCase.getNowPlayingMovies().results
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        _topRatedMovies.value = theMovieDatabaseApiUseCase.getTopRatedMovies().results
    }

    private fun getUpcomingMovies() = viewModelScope.launch {
        _upcomingMovies.value = theMovieDatabaseApiUseCase.getUpcomingMovies().results
    }

    private fun getUserProfileData() = viewModelScope.launch {
        val userId = authenticationRepository.verifyLogin().data?.uid ?: ""
        _userProfileData.value =
            profileRepository.getUserData(userId).data ?: UserProfileDataModel()
    }

    fun saveDataToSharedPreferences(key: String, movieId: String) {
        sharedPreferencesRepository.putString(key, movieId)
    }

}