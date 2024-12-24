package evan.ashley.plasma.controller;

import com.google.common.collect.ImmutableList;
import evan.ashley.plasma.dao.PostDao;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Optional;

@Log4j2
@RestController
public class PostController {

    @Autowired
    private PostDao postDao;

    @PostMapping("/post")
    public CreatePostResponse createPost(@RequestBody final CreatePostRequest request) throws ValidationException {
        final CreatePostOutput output = postDao.createPost(ImmutableCreatePostInput.builder()
                .postedById(request.getPostedById())
                .title(request.getTitle())
                .body(request.getBody())
                .build());
        return ImmutableCreatePostResponse.builder()
                .id(output.getId())
                .build();
    }

    @PatchMapping("/post/{id}")
    public void updatePost(
            @PathVariable("id") final String id,
            @RequestBody final UpdatePostRequest request) throws ResourceNotFoundException {
        postDao.updatePost(ImmutableUpdatePostInput.builder()
                .id(id)
                .title(request.getTitle())
                .body(request.getBody())
                .build());
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable("id") final String id) throws ResourceNotFoundException {
        postDao.deletePost(ImmutableDeletePostInput.builder()
                .id(id)
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
            @RequestParam("postedById") final String postedById,
            @RequestParam("maxPageSize") @Nullable final Integer maxPageSize,
            @RequestParam("sortOrder") @Nullable final PostsSortOrder sortOrder,
            @RequestParam("paginationToken") @Nullable final String paginationToken) {
        final ListPostsOutput output = postDao.listPosts(ImmutableListPostsInput.builder()
                .postedById(postedById)
                .maxPageSize(maxPageSize)
                .sortOrder(Optional.ofNullable(sortOrder)
                        .map(PostsSortOrder::toString)
                        .map(evan.ashley.plasma.model.dao.post.PostsSortOrder::valueOf)
                        .orElse(null))
                .paginationToken(paginationToken)
                .build());
        return ImmutableListPostsResponse.builder()
                .posts(output.getPosts().stream()
                        .map(Post::fromInternal)
                        .collect(ImmutableList.toImmutableList()))
                .paginationToken(output.getPaginationToken())
                .build();
    }
}
