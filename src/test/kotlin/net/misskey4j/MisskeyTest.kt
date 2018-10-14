package net.misskey4j

import net.misskey4j.adapters.HomeStreamAdapter
import net.misskey4j.adapters.MisskeyAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class MisskeyTest {

    companion object {
        private const val TOKEN = ""
    }

    private val thrown = ExpectedException.none()

    private val watcher = object : TestWatcher() {
        override fun failed(e: Throwable, description: Description) {
            e.printStackTrace()
        }
    }

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(thrown).around(watcher)


    fun testStream() {
        //thrown.expect(MisskeyException::class.java)

        val config = ConfigurationBuilder(TOKEN)
        config.setDomain("miss.tomocraft.net")
        val misskey = Misskey(config)
        misskey.addListener(object : MisskeyAdapter() {
            override fun updatedNote(note: Note) {
                println("ノートが成功しました！")
                println(note)
            }

            override fun gotMetaInfo(metaInfo: MetaInfo) {
                println("惑星の情報を取得しました！")
                println(metaInfo)
            }

            override fun gotMyInfo(myInfo: MyInfo) {
                println("自分の情報を取得しました！")
                println(myInfo)
            }

            override fun onException(ex: MisskeyException) {
                ex.printStackTrace()
            }
        })

        misskey.addStreamListener(StreamType.HOME, object : HomeStreamAdapter() {

        })

        misskey.getMetaInfo()
        misskey.getMyInfo()

        val noteUpdate = NoteUpdate("test note from kotlin")
        noteUpdate.isMobile = true
        noteUpdate.visibility = Visibility.PUBLIC

        misskey.createNote(noteUpdate)
    }

    @Test
    fun testNote() {
        val test = "{\"createdAt\":\"2018-08-26T02:52:33.971Z\",\"mediaIds\":[],\"replyId\":null,\"renoteId\":null,\"text\":\"リモートの誰かが何かをブーストしたら今のミスキーではTLの先頭にリノートとリノートされた投稿が二つ並ぶ。本来ならリノートされた投稿はTLのその時刻に表示されるべきではないのだが、「IDが採番された時刻」が新しいので出てしまう  #MisskeyApi\",\"cw\":null,\"tags\":[\"MisskeyApi\"],\"tagsLower\":[\"misskeyapi\"],\"userId\":\"5b737c8f152fee7cdbfac184\",\"viaMobile\":false,\"geo\":null,\"appId\":null,\"visibility\":\"public\",\"visibleUserIds\":[],\"_renote\":null,\"id\":\"5b8215f1f3e5b20042b2502a\",\"user\":{\"createdAt\":\"2018-08-15T01:06:23.192Z\",\"description\":\"Androidアプリを書いて暮らしてます\",\"followersCount\":28,\"followingCount\":13,\"name\":\"tateisu\",\"notesCount\":434,\"username\":\"tateisu\",\"host\":null,\"lastUsedAt\":\"2018-08-26T02:52:00.399Z\",\"avatarColor\":[134,132,124],\"avatarUrl\":\"https://s3.arkjp.net/misskey/drive/ae8d9a5c-457a-4029-8801-955442b4a5e2/15409_1976824491.jpg.cropped.png\",\"isLocked\":false,\"pendingReceivedFollowRequestsCount\":0,\"id\":\"5b737c8f152fee7cdbfac184\"},\"media\":[],\"prev\":null,\"next\":null}"
        val note = Misskey.gson.fromJson(test, Note::class.java)

        assert(!note.isMobile)
        assert(note.visibility == Visibility.PUBLIC)
        assert(note.fileIds.isEmpty())
        assert(note.replyId == null)
        assert(note.replyId == null)
        assert(note.text == "リモートの誰かが何かをブーストしたら今のミスキーではTLの先頭にリノートとリノートされた投稿が二つ並ぶ。本来ならリノートされた投稿はTLのその時刻に表示されるべきではないのだが、「IDが採番された時刻」が新しいので出てしまう  #MisskeyApi")
        assert(!note.user.isBot)
        assert(note.user.followersCount == 28)
        assert(note.user.followingCount == 13)
    }
}