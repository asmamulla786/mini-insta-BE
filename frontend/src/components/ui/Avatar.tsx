type AvatarProps = {
  name: string;
  src?: string;
  size?: 'sm' | 'md' | 'lg';
};

const sizeMap: Record<Required<AvatarProps>['size'], string> = {
  sm: 'h-8 w-8 text-xs',
  md: 'h-12 w-12 text-sm',
  lg: 'h-16 w-16 text-base'
};

export const Avatar = ({ name, src, size = 'md' }: AvatarProps) => {
  const initials = name
    .split(' ')
    .map((part) => part[0])
    .join('')
    .slice(0, 2)
    .toUpperCase();

  if (src) {
    return (
      <img
        src={src}
        alt={name}
        className={`${sizeMap[size]} rounded-full object-cover`}
      />
    );
  }

  return (
    <div
      className={`${sizeMap[size]} rounded-full bg-slate-800 text-center font-semibold leading-[inherit] text-white`}
    >
      <span className="flex h-full w-full items-center justify-center">
        {initials}
      </span>
    </div>
  );
};

