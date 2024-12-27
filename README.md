# Plasma (Server)

## Technology
* Maven
* Spring Boot
* PostgreSQL

## APIs
* User (see [UserController](./src/main/java/evan/ashley/plasma/controller/UserController.java) for more details)
  * `/user`
     * `POST` - Create a new user with a username and password
     * `GET` - Search for a user by username substring
  * `/user/{id}`
     * `GET` - Get information about a user
     * `PATCH` - Update a user (change username, password, email, etc.)
* Follow (see [FollowController](./src/main/java/evan/ashley/plasma/controller/FollowController.java) for more details)
  * `/follow`
     * `GET` - List followed users
  * `/follow/{id}`
     * `GET` - Fetch a follow by id
  * `/follow/user/{id}`
     * `POST` - Follow a user
     * `DELETE` - Unfollow a user
     * `GET` - Fetch a follow by user id
* Post (see [PostController](./src/main/java/evan/ashley/plasma/controller/PostController.java) for more details)
  * `/post`
     * `POST` - Create a new post
     * `GET` - List all posts from followers
  * `/post/{id}`
     * `GET` - Fetch a post
     * `UPDATE` - Update a post
     * `DELETE` - Delete a post
* Post (see [AuthController](./src/main/java/evan/ashley/plasma/controller/AuthController.java) for more details)
  * `/auth/login`
    * `POST` - Create a new session token
  * `/auth/identity`
     * `GET` - Determine the identity of a session

## Setup
This application was (almost) entirely developed and tested using [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/?section=mac#community-edition) (IDE),
[pgadmin](https://www.pgadmin.org/download/) (PostgreSQL management application), and [Bruno](https://www.usebruno.com/downloads) (api client).

Downloading these will likely produce the best results, however other IDEs or services may suffice in their place.

### Database (PostgreSQL) Setup
Ideally, there would be an automated set of scripts which enable plug-and-play with a clean PostgreSQL database. Unfortunately, this is not reality at this time.

Luckily, pgadmin offers a very nice interface for creating and managing table schemata, indices, triggers, and constraints.

First, create a database with the name `plasma`. This name is directly referenced in [MainComponent.java](./src/main/java/evan/ashley/plasma/MainComponent.java)
and used to construct the JDBC URL, but can certainly be changed as needed (with some refactoring).

#### Common

##### Extensions
* `pg_trgm` - used by the search user (`/user` `GET`) API
* `plpgsql` - the PostgreSQL query language

##### Triggers
1. Set id - on `INSERT`, add an `id` column
   ```sql
   BEGIN
     NEW.id = gen_random_uuid();
     RETURN NEW;
   END
   ```
1. Set creation time - on `INSERT`, add a `creation_time` column
   ```sql
   BEGIN
     NEW.creation_time = now();
     RETURN NEW;
   END
   ```
1. Set last modification time - on `INSERT` and `UPDATE`, add a `last_modification_time` column
   ```
   BEGIN
     NEW.last_modification_time = now();
     RETURN NEW;
   END
   ```
   
#### Tables

There are several tables that need to be created:
1. `users` - stores user data (username, password)
1. `posts` - stores user posts
1. `follows` - stores which users a given user follows
1. `sessions` - stores authenticated sessions

##### Users

###### Columns
* `id` (PK) - `character varying`
* `username` - `character varying`
* `password` (hashed and salted by BCrypt) - `character varying`
* `creation_time` - `timestamp with time zone`

###### Indexes
* `gist` on `username`; include `id` and `creation_time`

###### Constraints
* `id` - primary key constraint
* `username` - unique constraint

###### Triggers
* Set id on `INSERT`
* Set creation time on `UPDATE`

##### Posts

###### Columns
* `id` (PK) - `character varying`
* `posted_by_id` - `character varying`
* `creation_time` - `timestamp with time zone`
* `last_modification_time` - `timestamp with time zone`
* `title` - `character varying`
* `body` - `character varying`, nullable

###### Indexes
* `btree` on `posted_by_id` and `creation_time`; include `id`, `title`, and `body`

###### Constraints
* `id` - primary key constraint
* `posted_by_id` - foreign key constraint on `users.id`

###### Triggers
* Set id on `INSERT`
* Set creation time on `INSERT`
* Set last modification time on `UPDATE`

##### Follows

###### Columns
* `id` (PK) - `character varying`
* `follower_id` - `character varying`
* `followee_id` - `character varying`
* `creation_time` - `timestamp with time zone`

###### Indexes
* `btree` on `follower_id`; include `id`, `followee_id`, `creation_time`

###### Constraints
* `id` - primary key constraint
* `follower_id` - foreign key constraint on `users.id`
* `followee_id` - foreign key constraint on `users.id`
* `follower_id`, `creation_time` - unique constraint (one follow at a time)
* `follower_id`, `followee_id` - unique constraint (one follow of a user)
* Not following self (`follower_id != followee_id`)

###### Triggers
* Set id on `INSERT`
* Set creation time on `INSERT`

#### Sessions

##### Columns
* `id` - `character varying`
* `user_id` - `character varying`
* `creation_time` - `timestamp with time zone`

##### Constraints
* `id` - primary key constraint
* `user_id` - foreign key constraint on `users.id`

##### Triggers
* Set id on `INSERT`
* Set creation time on `INSERT`

### IDE Setup
To pull down dependencies, navigate to [pom.xml](./pom.xml) and click the Maven "Sync" button.

To build the application, navigate to Build in the toolbar and click "Build Project".

Finally, to run the application, navigate to Run in the toolbar and click "Run 'Main'".

### Notes

1. If run locally, the front-end runs on a different port than the backend server. This required CORS configuration in
   [GlobalConfigurer.java](./src/main/java/evan/ashley/plasma/GlobalConfigurer.java). If your front-end runs on a
   different port, or runs statically, the allowed origins may need to be updated.
2. Due to time constraints, there are (currently) no unit tests nor integration tests. If you encounter bugs or issues,
   please do not cut issues against the repository. Instead, please fork the repository and address the bugs as you see
   fit.