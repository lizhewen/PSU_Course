% downsample function
% input: image descriptor matrix im
% output: downsampled image descriptor matrix downsamplexy
function [downsamplexy] = downsample(im)
    % taking every other row, every other col of the picture
    downsamplexy = im(1:2:end,1:2:end,:);
end