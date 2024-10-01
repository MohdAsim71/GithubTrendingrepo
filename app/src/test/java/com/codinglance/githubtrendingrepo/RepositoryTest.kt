package com.codinglance.githubtrendingrepo

import com.codinglance.githubtrendingrepo.db.repoDao
import com.codinglance.githubtrendingrepo.model.DBData
import com.codinglance.githubtrendingrepo.model.Item
import com.codinglance.githubtrendingrepo.model.License
import com.codinglance.githubtrendingrepo.model.Owner
import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.network.ApiInterface
import com.codinglance.githubtrendingrepo.repository.Repository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var  repository: Repository
    private lateinit var apiInterface: ApiInterface
    private lateinit var userDao: repoDao
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp(){

        apiInterface = mockk()
        userDao = mockk()
        repository = Repository(apiInterface,userDao)
        Dispatchers.setMain(testDispatcher)  // Set the test dispatcher as the main dispatcher

    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()  // Reset the main dispatcher after the test
    }
    @Test
    fun testInsert() = runTest{
        val mockUserList = listOf(DBData(id = 1, name = "Test User", description = "hello"))
        coEvery { userDao.insertUser(mockUserList) } just Runs
        repository.insert(mockUserList)
        coVerify { userDao.insertUser(mockUserList) }

    }


    @Test
    fun testGetRepo() = runTest{
        val  spyRepository = spyk(repository)
        val  spyAPI = spyk(apiInterface)
        // Create a sample Item instance
        val item = Item(
            allow_forking = true,
            archive_url = "https://example.com/archive",
            archived = false,
            assignees_url = "https://example.com/assignees",
            blobs_url = "https://example.com/blobs",
            branches_url = "https://example.com/branches",
            clone_url = "https://example.com/clone",
            collaborators_url = "https://example.com/collaborators",
            comments_url = "https://example.com/comments",
            commits_url = "https://example.com/commits",
            compare_url = "https://example.com/compare",
            contents_url = "https://example.com/contents",
            contributors_url = "https://example.com/contributors",
            created_at = "2024-01-01T00:00:00Z",
            default_branch = "main",
            deployments_url = "https://example.com/deployments",
            description = "Sample description",
            disabled = false,
            downloads_url = "https://example.com/downloads",
            events_url = "https://example.com/events",
            fork = false,
            forks = 10,
            forks_count = 10,
            forks_url = "https://example.com/forks",
            full_name = "Full Repo Name",
            git_commits_url = "https://example.com/git_commits",
            git_refs_url = "https://example.com/git_refs",
            git_tags_url = "https://example.com/git_tags",
            git_url = "https://example.com/git",
            has_discussions = true,
            has_downloads = true,
            has_issues = true,
            has_pages = false,
            has_projects = true,
            has_wiki = false,
            homepage = "https://example.com/homepage",
            hooks_url = "https://example.com/hooks",
            html_url = "https://example.com/html",
            id = 123,
            is_template = false,
            issue_comment_url = "https://example.com/issue_comment",
            issue_events_url = "https://example.com/issue_events",
            issues_url = "https://example.com/issues",
            keys_url = "https://example.com/keys",
            labels_url = "https://example.com/labels",
            language = "Kotlin",
            languages_url = "https://example.com/languages",
            license = License("MIT","","","",""),
            merges_url = "https://example.com/merges",
            milestones_url = "https://example.com/milestones",
            mirror_url = "",
            name = "Repo Name",
            node_id = "node123",
            notifications_url = "https://example.com/notifications",
            open_issues = 5,
            open_issues_count = 5,
            owner = Owner("ownerName","","","","","","",""
                ,"","","","",false,"","","",""),
            private = false,
            pulls_url = "https://example.com/pulls",
            pushed_at = "2024-01-01T00:00:00Z",
            releases_url = "https://example.com/releases",
            score = 100.0,
            size = 1024,
            ssh_url = "git@github.com:example/repo.git",
            stargazers_count = 50,
            stargazers_url = "https://example.com/stargazers",
            statuses_url = "https://example.com/statuses",
            subscribers_url = "https://example.com/subscribers",
            subscription_url = "https://example.com/subscription",
            svn_url = "https://example.com/svn",
            tags_url = "https://example.com/tags",
            teams_url = "https://example.com/teams",
            topics = listOf("Kotlin", "Android"),
            trees_url = "https://example.com/trees",
            updated_at = "2024-01-01T00:00:00Z",
            url = "https://example.com/url",
            visibility = "public",
            watchers = 10,
            watchers_count = 10,
            web_commit_signoff_required = false
        )
        val mockResponse = RepoResponse(true, listOf( item),1)
        coEvery { repository.getRepo() } returns  mockResponse
        val  result = repository.getRepo()
        Assert.assertEquals(mockResponse,result)
    }



}