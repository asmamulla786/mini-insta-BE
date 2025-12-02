import { FormEvent, useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { EmptyState } from '../components/ui/EmptyState';
import type { Post } from '../types/api';
import { PostApi } from '../services/api';
import { PostCard } from '../components/posts/PostCard';

export const ProfilePage = () => {
  const { user } = useAuth();
  const [usernameQuery, setUsernameQuery] = useState(user?.username ?? '');
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchPosts = useCallback(async (username: string) => {
    if (!username) return;
    setLoading(true);
    try {
      const data = await PostApi.listByUser(username);
      setPosts(data);
      setError('');
    } catch (err) {
      setError('Unable to load posts for that username.');
      setPosts([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (user?.username) {
      void fetchPosts(user.username);
    }
  }, [fetchPosts, user?.username]);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await fetchPosts(usernameQuery.trim());
  };

  return (
    <div className="space-y-8">
      <div className="glass-panel p-6">
        <p className="text-xs uppercase tracking-[0.3em] text-slate-500">
          My profile
        </p>
        <h1 className="text-3xl font-semibold text-white">{user?.fullName}</h1>
        <p className="text-slate-400">
          <Link
            to={`/users/${user?.username ?? ''}`}
            className="underline-offset-2 hover:underline"
          >
            @{user?.username}
          </Link>
        </p>
        <div className="mt-4 flex flex-wrap gap-6 text-sm text-slate-300">
          <div>
            <p className="text-xs uppercase tracking-wide text-slate-500">
              Account type
            </p>
            <p>{user?.privateAccount ? 'Private' : 'Public'}</p>
          </div>
          <div>
            <p className="text-xs uppercase tracking-wide text-slate-500">
              Profile picture
            </p>
            {user?.profilePicUrl ? (
              <img
                className="mt-2 h-20 w-20 rounded-full object-cover"
                src={user.profilePicUrl}
                alt={user.fullName}
              />
            ) : (
              <p className="mt-2 text-slate-500">Not set</p>
            )}
          </div>
        </div>
      </div>

      <div className="glass-panel p-6">
        <div className="flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="text-xs uppercase tracking-[0.3em] text-slate-500">
              User explorer
            </p>
            <h2 className="text-xl font-semibold text-white">
              Browse posts by username
            </h2>
          </div>
          <form className="flex flex-col gap-3 md:flex-row" onSubmit={handleSubmit}>
            <Input
              name="username"
              placeholder="Search by username"
              value={usernameQuery}
              onChange={(e) => setUsernameQuery(e.target.value)}
            />
            <Button type="submit" variant="secondary" className="w-full md:w-auto">
              Load posts
            </Button>
          </form>
        </div>
        {error && <p className="mt-4 text-sm text-pink-400">{error}</p>}
      </div>

      {loading ? (
        <EmptyState
          title="Loading posts..."
          description="Fetching public posts for the selected user."
        />
      ) : posts.length ? (
        <div className="space-y-6">
          {posts.map((post) => (
            <PostCard
              key={post.id}
              post={post}
              onRefresh={() => fetchPosts(usernameQuery)}
            />
          ))}
        </div>
      ) : (
        <EmptyState
          title="No posts to show"
          description="Search for a username to see their posts."
        />
      )}
    </div>
  );
};

