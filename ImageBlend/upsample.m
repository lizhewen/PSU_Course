function [upsamplexy] = upsample(img)
    xsize = length(img(:,1,1));
    ysize = length(img(1,:,1));
    zsize = length(img(1,1,:));
    upsamplexy = zeros(xsize*2, ysize*2, zsize,'double');
    upsamplexy(1:2:end,1:2:end,:) = img(:,:,:);
    [~,upsamplexy] = gausBlur(upsamplexy);
    upsamplexy = uint8(4*upsamplexy);
end