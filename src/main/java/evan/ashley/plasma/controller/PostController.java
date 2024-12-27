package evan.ashley.plasma.controller;

import com.google.common.collect.ImmutableList;
import evan.ashley.plasma.constant.Header;
import evan.ashley.plasma.dao.PostDao;
import evan.ashley.plasma.dao.SessionDao;
import evan.ashley.plasma.model.api.post.CreatePostRequest;
import evan.ashley.plasma.model.api.post.CreatePostResponse;
import evan.ashley.plasma.model.api.post.GetPostResponse;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.api.post.ImmutableCreatePostResponse;
import evan.ashley.plasma.model.api.post.ImmutableGetPostResponse;
import evan.ashley.plasma.model.api.post.ImmutableListPostsResponse;
import evan.ashley.plasma.model.api.post.ListPostsResponse;
import evan.ashley.plasma.model.api.post.Post;
import evan.ashley.plasma.model.api.post.PostsSortOrder;
import evan.ashley.plasma.model.api.post.UpdatePostRequest;
import evan.ashley.plasma.model.dao.follow.ImmutableDeleteFollowInput;
import evan.ashley.plasma.model.dao.post.CreatePostOutput;
import evan.ashley.plasma.model.dao.post.GetPostOutput;
import evan.ashley.plasma.model.dao.post.ImmutableCreatePostInput;
import evan.ashley.plasma.model.dao.post.ImmutableDeletePostInput;
import evan.ashley.plasma.model.dao.post.ImmutableGetPostInput;
import evan.ashley.plasma.model.dao.post.ImmutableListPostsInput;
import evan.ashley.plasma.model.dao.post.ImmutableUpdatePostInput;
import evan.ashley.plasma.model.dao.post.ListPostsOutput;
import evan.ashley.plasma.model.dao.session.GetSessionOutput;
import evan.ashley.plasma.model.dao.session.ImmutableGetSessionInput;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Optional;

@Log4j2
@RestController
public class PostController {

    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private PostDao postDao;

    @PostMapping("/post")
    public CreatePostResponse createPost(
            @RequestHeader(Header.SESSION) final String sessionId,
            @RequestBody final CreatePostRequest request) throws ValidationException, ResourceNotFoundException {
        final GetSessionOutput getSessionOutput = sessionDao.getSession(ImmutableGetSessionInput.builder()
                .id(sessionId)
                .build());
        final CreatePostOutput output = postDao.createPost(ImmutableCreatePostInput.builder()
                .postedById(getSessionOutput.getUserId())
                .title(request.getTitle())
                .body(request.getBody())
                .build());
        return ImmutableCreatePostResponse.builder()
                .id(output.getId())
                .build();
    }

    @PatchMapping("/post/{id}")
    public void updatePost(
            @RequestHeader(Header.SESSION) final String sessionId,
            @PathVariable("id") final String id,
            @RequestBody final UpdatePostRequest request) throws ResourceNotFoundException {
        final GetSessionOutput getSessionOutput = sessionDao.getSession(ImmutableGetSessionInput.builder()
                .id(sessionId)
                .build());
        postDao.updatePost(ImmutableUpdatePostInput.builder()
                .id(id)
                .postedById(getSessionOutput.getUserId())
                .title(request.getTitle())
                .body(request.getBody())
                .build());
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(
            @RequestHeader(Header.SESSION) final String sessionId,
            @PathVariable("id") final String id) throws ResourceNotFoundException {
        final GetSessionOutput getSessionOutput = sessionDao.getSession(ImmutableGetSessionInput.builder()
                .id(sessionId)
                .build());
        postDao.deletePost(ImmutableDeletePostInput.builder()
                .id(id)
                .postedById(getSessionOutput.getUserId())
                .build());
    }

    @GetMapping("/post/{id}")
    public GetPostResponse getPost(@PathVariable("id") final String id) throws ResourceNotFoundException {
        final GetPostOutput output = postDao.getPost(ImmutableGetPostInput.builder()
                .id(id)
                .build());
        return ImmutableGetPostResponse.builder()
                .id(output.getId())
                .postedById(output.getPostedById())
                .creationTime(output.getCreationTime())
                .lastModificationTime(output.getLastModificationTime())
                .title(output.getTitle())
                .body(output.getBody())
                .build();
    }

    @GetMapping("/post")
    public ListPostsResponse listPosts(
            @RequestHeader(Header.SESSION) @Nullable final String sessionId,
            @RequestParam("postedById") @Nullable final String postedById,
            @RequestParam("maxPageSize") @Nullable final Integer maxPageSize,
            @RequestParam("sortOrder") @Nullable final PostsSortOrder sortOrder,
            @RequestParam("paginationToken") @Nullable final String paginationToken) throws ResourceNotFoundException, ValidationException {
        final ImmutableListPostsInput.Builder listPostsInputBuilder = ImmutableListPostsInput.builder()
                .maxPageSize(maxPageSize)
                .sortOrder(Optional.ofNullable(sortOrder)
                        .map(PostsSortOrder::toString)
                        .map(evan.ashley.plasma.model.dao.post.PostsSortOrder::valueOf)
                        .orElse(null))
                .paginationToken(paginationToken);
        if (postedById != null) {
            listPostsInputBuilder
                .postedById(postedById);
        } else if (sessionId != null) {
            final GetSessionOutput getSessionOutput = sessionDao.getSession(ImmutableGetSessionInput.builder()
                    .id(sessionId)
                    .build());
            listPostsInputBuilder
                    .followerId(getSessionOutput.getUserId());
        }
        final ListPostsOutput output = postDao.listPosts(listPostsInputBuilder.build());
        return ImmutableListPostsResponse.builder()
                .posts(output.getPosts().stream()
                        .map(Post::fromInternal)
                        .collect(ImmutableList.toImmutableList()))
                .paginationToken(output.getPaginationToken())
                .build();
    }
}
